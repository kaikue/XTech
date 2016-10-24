package kaikue.xtech.tileentities;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityItemInserter extends TileEntityInserter implements ITickable {

	public TileEntityItemInserter() {
		super();
	}

	public TileEntityItemInserter(IBlockState facing) {
		super(facing);
	}

	@Override
	protected boolean isReceiverAt(BlockPos checkPos) {
		return inventoryAt(checkPos) != null;
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

	@Override
	protected boolean transfer(BlockPos destPos, EnumFacing facing) {
		IInventory dest = inventoryAt(destPos);
		if(dest == null) return false;
		
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
