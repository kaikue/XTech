package kaikue.xtech.tileentities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kaikue.xtech.Config;
import kaikue.xtech.XTech;
import kaikue.xtech.blocks.BlockMirror;
import kaikue.xtech.blocks.BlockSplitter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

public abstract class TileEntityInserter extends TileEntity implements ITickable {

	private class Receiver {
		BlockPos pos;
		int distance;
		EnumFacing direction;
		int reduction;
		public Receiver(BlockPos pos, int distance, EnumFacing direction, int reduction) {
			this.pos = pos;
			this.distance = distance;
			this.direction = direction;
			this.reduction = reduction;
		}
	}

	private List<Receiver> receivers = new ArrayList<Receiver>();
	public boolean justTransferred;
	public ArrayList<BlockPos> segments = new ArrayList<BlockPos>();
	protected int destCheckCooldown;
	protected int insertCooldown;
	public EnumFacing facing;

	//Check if the position is the correct type for inserting contents
	abstract protected boolean isReceiverAt(BlockPos checkPos, EnumFacing face);

	//Transfer contents into the TileEntity at destPos
	abstract protected boolean transfer(BlockPos destPos, EnumFacing facing, int reduction);

	public TileEntityInserter() {
		resetDestCheckCooldown();
		resetInsertCooldown();
	}

	public TileEntityInserter(EnumFacing facing) {
		this();
		this.facing = facing;
	}

	@Override
	public void update() {
		if(worldObj.isRemote) return;
		if(!shouldUpdate()) return;

		boolean foundReceiver = true;

		insertCooldown--;
		if(insertCooldown < 1) {
			boolean transferred = false;
			if(worldObj.isBlockIndirectlyGettingPowered(pos) == 0) {
				//XTech.logger.info("starting insert");
				for(Receiver receiver : receivers) {
					if(isReceiverAt(receiver.pos, receiver.direction)) {
						//XTech.logger.info("inserted into " + receiver.pos + ", factor " + receiver.reduction);
						transferred |= transfer(receiver.pos, receiver.direction, receiver.reduction);
					}
					else {
						foundReceiver = false;
					}
				}
			}
			resetInsertCooldown();
			if(transferred != justTransferred) {
				justTransferred = transferred;
				updateInWorld();
			}
		}

		destCheckCooldown--;
		if(destCheckCooldown < 1 || !foundReceiver) {
			findClosestReceivers();
			//if changed?
			updateInWorld();
			resetDestCheckCooldown();
		}
	}

	private void findClosestReceivers() {
		segments = new ArrayList<BlockPos>();
		receivers = new ArrayList<Receiver>();
		Queue<Receiver> queue = new LinkedList<Receiver>();
		Receiver start = new Receiver(pos, 0, facing, 1);
		queue.add(start);
		while(!queue.isEmpty()) {
			Receiver current = queue.remove();
			MutableBlockPos checkPos = new MutableBlockPos(current.pos);
			EnumFacing direction = current.direction;
			int reduction = current.reduction;
			BlockPos segmentStart = current.pos;
			for(int i = current.distance; i < Config.beamDistance; i++) {
				checkPos.move(direction);

				//don't check out of bounds
				if(!getWorld().isBlockLoaded(checkPos)) break;

				if(isReceiverAt(checkPos, direction.getOpposite())) {
					BlockPos p = checkPos.toImmutable();
					Receiver r = new Receiver(p, i, direction.getOpposite(), reduction);
					receivers.add(r);
					segments.add(segmentStart);
					segments.add(p);
					break; //TODO: don't do this if it's an advanced inserter (can beam through inserters)
				}

				IBlockState blockState = getWorld().getBlockState(checkPos);

				if(blockState.getBlock() instanceof BlockMirror) {
					BlockPos p = checkPos.toImmutable();
					EnumFacing newDirection = turnDirection(direction, getStateFacing(blockState));
					if(newDirection == null) break; //hit the back of a mirror
					//new direction may need to be flipped- splitters are bidirectional
					if(blockState.getBlock() instanceof BlockSplitter) {
						reduction *= 2;
						Receiver state = new Receiver(p, i, direction, reduction); //current direction
						queue.add(state);
					}
					direction = newDirection;
					segments.add(segmentStart);
					segments.add(p);
					segmentStart = p;
				}

				if(blockState.isOpaqueCube()) {
					break;
				}
			}
		}
	}

	private EnumFacing turnDirection(EnumFacing original, BlockMirror.EnumOrientation mirror) {
		if(mirror.isHorizontal()) {
			if(original.getAxis() == Axis.Y) return null;
			EnumFacing horiz = mirror.getFacing();
			if(horiz == original.getOpposite()) {
				return original.rotateY();
			}
			if(horiz == original.rotateYCCW()) {
				return horiz;
			}
			return null;
		}

		//mirror is up or down
		EnumFacing horizComponent = mirror.getHorizontalComponent();
		EnumFacing verticalComponent = mirror.getFacing();
		if(original == horizComponent.getOpposite()) {
			return verticalComponent;
		}
		if(original == verticalComponent.getOpposite()) {
			return horizComponent;
		}

		return null;
	}

	protected BlockMirror.EnumOrientation getStateFacing(IBlockState blockState) {
		return blockState.getValue(BlockMirror.FACING);
	}

	protected void resetDestCheckCooldown() {
		destCheckCooldown = 20;
	}

	protected void resetInsertCooldown() {
		insertCooldown = 10;
	}

	protected boolean shouldUpdate() {
		//Override this if you don't want to check for update every tick
		return true;
	}
	
	private void updateInWorld() {
		if (worldObj != null) {
			IBlockState state = worldObj.getBlockState(getPos());
			worldObj.notifyBlockUpdate(getPos(), state, state, 3);
		}
		markDirty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("facing", facing.getIndex());
		compound.setBoolean("justTransferred", justTransferred);
		int[] segmentsX = new int[segments.size()];
		for(int i = 0; i < segments.size(); i++) {
			segmentsX[i] = segments.get(i).getX();
		}
		compound.setIntArray("segmentsX", segmentsX);
		int[] segmentsY = new int[segments.size()];
		for(int i = 0; i < segments.size(); i++) {
			segmentsY[i] = segments.get(i).getY();
		}
		compound.setIntArray("segmentsY", segmentsY);
		int[] segmentsZ = new int[segments.size()];
		for(int i = 0; i < segments.size(); i++) {
			segmentsZ[i] = segments.get(i).getZ();
		}
		compound.setIntArray("segmentsZ", segmentsZ);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.facing = EnumFacing.getFront(compound.getInteger("facing"));
		this.justTransferred = compound.getBoolean("justTransferred");
		this.segments = new ArrayList<BlockPos>();
		int[] segmentsX = compound.getIntArray("segmentsX");
		int[] segmentsY = compound.getIntArray("segmentsY");
		int[] segmentsZ = compound.getIntArray("segmentsZ");
		for(int i = 0; i < segmentsX.length; i++) {
			this.segments.add(new BlockPos(segmentsX[i], segmentsY[i], segmentsZ[i]));
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}
}
