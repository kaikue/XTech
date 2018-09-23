package kaikue.xtech.beamnetwork;

import kaikue.xtech.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class NetworkItemInserter extends NetworkInserter {

	public NetworkItemInserter() {
		super();
	}

	public NetworkItemInserter(EnumFacing facing) {
		super(facing);
	}

	@Override
	protected boolean isReceiverAt(World world, BlockPos checkPos, EnumFacing face) {
		return inventoryAt(world, checkPos, face) != null;
	}

	public static TileEntity inventoryAt(World world, BlockPos checkPos, EnumFacing face) {
		IBlockState blockState = world.getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = world.getTileEntity(checkPos);
			if(tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face)) {
				return tileEntity;
			}
		}
		return null;
	}

	@Override
	protected boolean transfer(World world, BlockPos myPos, BlockPos destPos, EnumFacing face, int reduction) {
		TileEntity dest = inventoryAt(world, destPos, face);
		if(dest == null) return false;

		TileEntity source = inventoryAt(world, myPos.offset(facing.getOpposite()), facing);
		if(source == null) return false;

		IItemHandler destHandler = dest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
		IItemHandler sourceHandler = source.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);

		for(int i = 0; i < sourceHandler.getSlots(); i++) {
			ItemStack itemsToTransfer = sourceHandler.extractItem(i, Config.itemsTransfer / reduction, true);
			if (itemsToTransfer != null) {
				for (int j = 0; j < destHandler.getSlots(); j++) {
					ItemStack remainder = destHandler.insertItem(j, itemsToTransfer, false);
					int r = remainder == null ? 0 : remainder.getCount();
					int q = itemsToTransfer.getCount() - r;
					sourceHandler.extractItem(i, q, false);
					if(q > 0) return true;
				}
			}
		}
		return false;
	}

}
