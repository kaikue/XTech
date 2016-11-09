package kaikue.xtech.beamnetwork;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kaikue.xtech.Config;
import kaikue.xtech.blocks.BlockMirror;
import kaikue.xtech.blocks.BlockSplitter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public abstract class NetworkInserter {

	public class Receiver {
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

	public List<Receiver> receivers = new ArrayList<Receiver>();
	public boolean justTransferred;
	public ArrayList<BlockPos> segments = new ArrayList<BlockPos>();
	protected int destCheckCooldown;
	protected int insertCooldown;
	public EnumFacing facing;

	//Check if the position is the correct type for inserting contents
	abstract protected boolean isReceiverAt(World world, BlockPos checkPos, EnumFacing face);

	//Transfer contents into the TileEntity at destPos
	abstract protected boolean transfer(World world, BlockPos myPos, BlockPos destPos, EnumFacing facing, int reduction);

	public NetworkInserter() {
		resetDestCheckCooldown();
		resetInsertCooldown();
	}

	public NetworkInserter(EnumFacing facing) {
		this();
		this.facing = facing;
	}

	public boolean update(World world, BlockPos pos) {
		if(world.isRemote) return false;
		if(!shouldUpdate(world)) return false;

		boolean isChanged = false;
		boolean foundReceiver = true;

		insertCooldown--;
		if(insertCooldown < 1) {
			boolean transferred = false;
			if(world.isBlockIndirectlyGettingPowered(pos) == 0) {
				for(Receiver receiver : receivers) {
					if(isReceiverAt(world, receiver.pos, receiver.direction)) {
						transferred |= transfer(world, pos, receiver.pos, receiver.direction, receiver.reduction);
					}
					else {
						foundReceiver = false;
					}
				}
			}
			resetInsertCooldown();
			if(transferred != justTransferred) {
				justTransferred = transferred;
				isChanged = true;
			}
		}

		destCheckCooldown--;
		if(destCheckCooldown < 1 || !foundReceiver) {
			findClosestReceivers(world, pos);
			//if changed?
			isChanged = true;
			resetDestCheckCooldown();
		}
		return isChanged;
	}

	private void findClosestReceivers(World world, BlockPos pos) {
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
				if(!world.isBlockLoaded(checkPos)) break;

				if(isReceiverAt(world, checkPos, direction.getOpposite())) {
					BlockPos p = checkPos.toImmutable();
					Receiver r = new Receiver(p, i, direction.getOpposite(), reduction);
					receivers.add(r);
					segments.add(segmentStart);
					segments.add(p);
					break; //TODO: don't do this if it's an advanced inserter (can beam through inserters)
				}

				IBlockState blockState = world.getBlockState(checkPos);

				Block block = blockState.getBlock();
				if(block instanceof BlockMirror) {
					BlockPos p = checkPos.toImmutable();
					EnumFacing newDirection = turnDirection(direction, getStateFacing(blockState));
					if(newDirection == null) {
						EnumFacing otherDirection = turnDirection(direction.getOpposite(), getStateFacing(blockState));
						if(otherDirection != null && block instanceof BlockSplitter) {
							newDirection = otherDirection.getOpposite();
						}
						else {
							break;
						}
					}
					if(block instanceof BlockSplitter) {
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

	protected boolean shouldUpdate(World world) {
		//Override this if you don't want to check for update every tick
		return true;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
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

	public void readFromNBT(NBTTagCompound compound) {
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

}
