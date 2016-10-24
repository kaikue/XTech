package kaikue.xtech.tileentities;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityItemInserter extends TileEntityInserter implements ITickable {

	public TileEntityItemInserter() {
		super();
	}

	public TileEntityItemInserter(IBlockState facing) {
		super(facing);
	}

	@Override
	protected boolean isReceiverAt(BlockPos checkPos, EnumFacing face) {
		return inventoryAt(checkPos, face) != null;
	}

	private TileEntity inventoryAt(BlockPos checkPos, EnumFacing face) {
		IBlockState blockState = getWorld().getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = getWorld().getTileEntity(checkPos);
			if(tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face)) {
				return tileEntity;
			}
		}
		return null;
	}

	@Override
	protected boolean transfer(BlockPos destPos, EnumFacing face) {
		TileEntity dest = inventoryAt(destPos, face);
		if(dest == null) return false;

		TileEntity source = inventoryAt(pos.offset(getStateFacing(this.facing).getOpposite()), getStateFacing(this.facing));
		if(source == null) return false;

		IItemHandler destHandler = dest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
		IItemHandler sourceHandler = source.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getStateFacing(this.facing));

		for(int i = 0; i < sourceHandler.getSlots(); i++) {
			ItemStack itemToTransfer = sourceHandler.extractItem(i, 1, true);
			if (itemToTransfer != null) {
				for (int j = 0; j < destHandler.getSlots(); j++) {
					ItemStack remainder = destHandler.insertItem(j, itemToTransfer, false);
					if(remainder != null) {
						continue;
					}
					sourceHandler.extractItem(i, 1, false);
					return true;
				}
			}
		}
		return false;
	}

}
