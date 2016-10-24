package kaikue.xtech.tileentities;

import java.util.ArrayList;

import kaikue.xtech.Config;
import kaikue.xtech.ModBlocks;
import kaikue.xtech.blocks.DirectionalBaseBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

public abstract class TileEntityInserter extends TileEntity implements ITickable {

	public BlockPos receiverPos;
	public boolean justTransferred;
	public ArrayList<BlockPos> mirrors = new ArrayList<BlockPos>();
	private int destCheckCooldown;
	private int insertCooldown;
	protected EnumFacing facing;
	private EnumFacing insertFace;

	//Check if the position is the correct type for inserting contents
	abstract protected boolean isReceiverAt(BlockPos checkPos, EnumFacing face);

	//Transfer contents into the TileEntity at destPos
	abstract protected boolean transfer(BlockPos destPos, EnumFacing facing);

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

		boolean foundReceiver = true;

		insertCooldown--;
		if(insertCooldown < 1) {
			boolean transferred = false;
			if(receiverPos != null && isReceiverAt(receiverPos, insertFace)) {
				if(worldObj.isBlockIndirectlyGettingPowered(pos) == 0) {
					transferred = transfer(receiverPos, insertFace);
				}
			}
			else {
				foundReceiver = false;
			}
			resetInsertCooldown();
			if(transferred != justTransferred) {
				justTransferred = transferred;
				updateInWorld();
			}
		}

		destCheckCooldown--;
		if(destCheckCooldown < 1 || !foundReceiver) {
			BlockPos oldReceiverPos = receiverPos;
			findClosestReceiver();
			if((receiverPos == null && oldReceiverPos != null) || (receiverPos != null && !receiverPos.equals(oldReceiverPos))) {
				updateInWorld();
			}
			resetDestCheckCooldown();
		}
	}

	private void findClosestReceiver() {
		mirrors = new ArrayList<BlockPos>();
		EnumFacing direction = facing;
		MutableBlockPos checkPos = new MutableBlockPos(pos);
		for(int i = 0; i < Config.beamDistance; i++) {
			checkPos.move(direction);

			//don't check out of bounds
			if(!getWorld().isBlockLoaded(checkPos)) break;

			if(isReceiverAt(checkPos, direction.getOpposite())) {
				receiverPos = checkPos.toImmutable();
				insertFace = direction.getOpposite();
				return;
			}

			IBlockState blockState = getWorld().getBlockState(checkPos);

			if(blockState.getBlock() == ModBlocks.blockMirror) {
				direction = turnDirection(direction, getStateFacing(blockState));
				mirrors.add(checkPos.toImmutable());
				if(direction == null) break; //hit the back of a mirror
			}

			if(blockState.isOpaqueCube()) {
				break;
			}
		}
		receiverPos = null;
	}

	private EnumFacing turnDirection(EnumFacing original, EnumFacing mirror) {
		if(mirror == original.getOpposite()) {
			return original.rotateY();
		}
		if(mirror == original.rotateYCCW()) {
			return mirror;
		}
		return null;
	}

	protected EnumFacing getStateFacing(IBlockState blockState) {
		return blockState.getValue(DirectionalBaseBlock.FACING);
	}

	protected void resetDestCheckCooldown() {
		destCheckCooldown = 20;
	}

	protected void resetInsertCooldown() {
		insertCooldown = 10;
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
		int[] mirrorsX = new int[mirrors.size()];
		for(int i = 0; i < mirrors.size(); i++) {
			mirrorsX[i] = mirrors.get(i).getX();
		}
		compound.setIntArray("mirrorsX", mirrorsX);
		int[] mirrorsY = new int[mirrors.size()];
		for(int i = 0; i < mirrors.size(); i++) {
			mirrorsY[i] = mirrors.get(i).getY();
		}
		compound.setIntArray("mirrorsY", mirrorsY);
		int[] mirrorsZ = new int[mirrors.size()];
		for(int i = 0; i < mirrors.size(); i++) {
			mirrorsZ[i] = mirrors.get(i).getZ();
		}
		compound.setIntArray("mirrorsZ", mirrorsZ);
		if(receiverPos != null) compound.setLong("receiverPos", receiverPos.toLong());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.facing = EnumFacing.getFront(compound.getInteger("facing"));
		this.justTransferred = compound.getBoolean("justTransferred");
		this.mirrors = new ArrayList<BlockPos>();
		int[] mirrorsX = compound.getIntArray("mirrorsX");
		int[] mirrorsY = compound.getIntArray("mirrorsY");
		int[] mirrorsZ = compound.getIntArray("mirrorsZ");
		for(int i = 0; i < mirrorsX.length; i++) {
			this.mirrors.add(new BlockPos(mirrorsX[i], mirrorsY[i], mirrorsZ[i]));
		}
		this.receiverPos = compound.hasKey("receiverPos") ? BlockPos.fromLong(compound.getLong("receiverPos")) : null;
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
