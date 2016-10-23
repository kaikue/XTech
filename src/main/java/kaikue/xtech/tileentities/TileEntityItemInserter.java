package kaikue.xtech.tileentities;

import java.util.ArrayList;

import javax.annotation.Nullable;

import kaikue.xtech.Config;
import kaikue.xtech.ModBlocks;
import kaikue.xtech.blocks.DirectionalBaseBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

public class TileEntityItemInserter extends TileEntity implements ITickable {

	public BlockPos inventoryPos;
	public boolean justTransferred;
	public ArrayList<BlockPos> mirrors = new ArrayList<BlockPos>();
	private IBlockState facing;
	private EnumFacing insertFacing;
	private int invCheckCooldown;
	private int insertCooldown;

	public TileEntityItemInserter() {
		resetInvCheckCooldown();
		resetInsertCooldown();
	}

	public TileEntityItemInserter(IBlockState facing) {
		this();
		this.facing = facing;
	}

	@Override
	public void update() {
		if(worldObj.isRemote) return;

		boolean foundInventory = true;

		if(worldObj.isBlockIndirectlyGettingPowered(pos) == 0) {
			insertCooldown--;
			if(insertCooldown < 1) {
				if(inventoryPos != null) {
					IInventory inventory = inventoryAt(inventoryPos);
					if(inventory != null) {
						boolean transferred = transferItem(inventory, insertFacing);
						if(transferred != justTransferred) {
							justTransferred = transferred;
							updateInWorld();
						}
					}
					else {
						foundInventory = false;
					}
				}
				else {
					foundInventory = false;
				}
				resetInsertCooldown();
			}
		}
		invCheckCooldown--;
		if(invCheckCooldown < 1 || !foundInventory) {
			BlockPos oldInventoryPos = inventoryPos;
			findClosestInventory();
			if((inventoryPos == null && oldInventoryPos != null) || (inventoryPos != null && !inventoryPos.equals(oldInventoryPos))) {
				updateInWorld();
			}
			resetInvCheckCooldown();
		}
	}

	private void findClosestInventory() {
		mirrors = new ArrayList<BlockPos>();
		EnumFacing direction = getStateFacing(facing);
		MutableBlockPos checkPos = new MutableBlockPos(pos);
		for(int i = 0; i < Config.beamDistance; i++) {
			checkPos.move(direction);

			//don't check out of bounds
			if(!getWorld().isBlockLoaded(checkPos)) break;

			IBlockState blockState = getWorld().getBlockState(checkPos);
			if(blockState.getBlock().hasTileEntity(blockState)) {
				TileEntity tileEntity = getWorld().getTileEntity(checkPos);
				if (tileEntity != null && tileEntity instanceof IInventory) {
					inventoryPos = checkPos.toImmutable();
					insertFacing = direction;
					return;
				}
			}

			if(blockState.getBlock() == ModBlocks.blockMirror) {
				direction = turnDirection(direction, getStateFacing(blockState));
				mirrors.add(checkPos.toImmutable());
				if(direction == null) break; //hit the back of a mirror
			}

			if(blockState.isOpaqueCube()) {
				break;
			}
		}
		inventoryPos = null;
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

	private IInventory inventoryAt(BlockPos checkPos) {
		IBlockState blockState = getWorld().getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = getWorld().getTileEntity(checkPos);
			if(tileEntity != null && tileEntity instanceof IInventory) {
				return (IInventory)tileEntity;
			}
		}
		return null;
	}

	private boolean transferItem(IInventory dest, EnumFacing facing) {
		IInventory source = inventoryAt(pos.offset(getStateFacing(this.facing).getOpposite()));
		if(source == null) return false;
		
		for(int i = 0; i < source.getSizeInventory(); i++) {
			if(source.getStackInSlot(i) != null) {
				ItemStack itemstackSrc = source.getStackInSlot(i).copy();
				ItemStack itemstackDest = putStackInInventoryAllSlots(dest, source.decrStackSize(i, 1), facing);
				
				if(itemstackDest == null || itemstackDest.stackSize == 0) {
					source.markDirty();
					return true;
				}
				
				source.setInventorySlotContents(i, itemstackSrc);
			}
		}
		return false;
	}

	private EnumFacing getStateFacing(IBlockState blockState) {
		return blockState.getValue(DirectionalBaseBlock.FACING);
	}

	private void resetInvCheckCooldown() {
		invCheckCooldown = 20;
	}

	private void resetInsertCooldown() {
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
		compound.setInteger("facing", ModBlocks.blockItemInserter.getMetaFromState(this.facing));
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
		if(inventoryPos != null) compound.setLong("inventoryPos", inventoryPos.toLong());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.facing = ModBlocks.blockItemInserter.getStateFromMeta(compound.getInteger("facing"));
		this.justTransferred = compound.getBoolean("justTransferred");
		this.mirrors = new ArrayList<BlockPos>();
		int[] mirrorsX = compound.getIntArray("mirrorsX");
		int[] mirrorsY = compound.getIntArray("mirrorsY");
		int[] mirrorsZ = compound.getIntArray("mirrorsZ");
		for(int i = 0; i < mirrorsX.length; i++) {
			this.mirrors.add(new BlockPos(mirrorsX[i], mirrorsY[i], mirrorsZ[i]));
		}
		this.inventoryPos = compound.hasKey("inventoryPos") ? BlockPos.fromLong(compound.getLong("inventoryPos")) : null;
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

	//Inventory stuff
	//Code taken from TileEntityHopper

	private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
		return !inventoryIn.isItemValidForSlot(index, stack) ? false : 
			!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
	}

	private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() != stack2.getItem() ? false : 
			(stack1.getMetadata() != stack2.getMetadata() ? false : 
				(stack1.stackSize > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
	}

	public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, @Nullable EnumFacing side) {
		if(inventoryIn instanceof ISidedInventory && side != null) {
			ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
			int[] aint = isidedinventory.getSlotsForFace(side);

			for(int k = 0; k < aint.length && stack != null && stack.stackSize > 0; k++) {
				stack = insertStack(inventoryIn, stack, aint[k], side);
			}
		}
		else {
			int i = inventoryIn.getSizeInventory();

			for(int j = 0; j < i && stack != null && stack.stackSize > 0; j++) {
				stack = insertStack(inventoryIn, stack, j, side);
			}
		}

		if(stack != null && stack.stackSize == 0) {
			stack = null;
		}

		return stack;
	}

	private static ItemStack insertStack(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
		ItemStack itemstack = inventoryIn.getStackInSlot(index);

		if (canInsertItemInSlot(inventoryIn, stack, index, side)) {
			boolean transferred = false;

			if (itemstack == null) {
				int max = Math.min(stack.getMaxStackSize(), inventoryIn.getInventoryStackLimit());
				if (max >= stack.stackSize) {
					inventoryIn.setInventorySlotContents(index, stack);
					stack = null;
				}
				else {
					inventoryIn.setInventorySlotContents(index, stack.splitStack(max));
				}
				transferred = true;
			}
			else if (canCombine(itemstack, stack)) {
				int max = Math.min(stack.getMaxStackSize(), inventoryIn.getInventoryStackLimit());
				if (max > itemstack.stackSize) {
					int i = max - itemstack.stackSize;
					int j = Math.min(stack.stackSize, i);
					stack.stackSize -= j;
					itemstack.stackSize += j;
					transferred = j > 0;
				}
			}

			if (transferred) {
				inventoryIn.markDirty();
			}
		}

		return stack;
	}

}
