package kaikue.xtech.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBeamNetwork extends Container {

	private TileEntityBeamNetwork te;

	public ContainerBeamNetwork(IInventory playerInventory, TileEntityBeamNetwork te) {
		this.te = te;
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory) {
		//Main inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 9 + col * 18;
				int y = row * 18 + 70;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
			}
		}

		//Hotbar
		for (int row = 0; row < 9; ++row) {
			int x = 9 + row * 18;
			int y = 58 + 70;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	private void addOwnSlots() {
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int x = 9;
		int y = 6;

		int slotIndex = 0;
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
			slotIndex++;
			x += 18;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		//TODO rework this a lot
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(index < itemHandler.getSlots()) {
				if(!this.mergeItemStack(itemstack1, itemHandler.getSlots(), this.inventorySlots.size(), true)) {
					return null;
				}
			}
			else if(!this.mergeItemStack(itemstack1, 0, itemHandler.getSlots(), false)) {
				return null;
			}

			if(itemstack1.getCount() == 0) {
				slot.putStack(null);
			}
			else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}
}
