package kaikue.xtech.tileentities;

import kaikue.xtech.XTech;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityGenerator extends TileEntityInserter implements ITickable {

	public TileEntityGenerator() {
		super();
	}

	public TileEntityGenerator(EnumFacing facing) {
		super(facing);
	}

	@Override
	protected boolean isReceiverAt(BlockPos checkPos, EnumFacing face) {
		return consumerAt(checkPos, face) != null;
	}
	
	@Override
	protected void resetInsertCooldown() {
		insertCooldown = 2;
	}

	private TileEntity consumerAt(BlockPos checkPos, EnumFacing face) {
		IBlockState blockState = getWorld().getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = getWorld().getTileEntity(checkPos);
			if(tileEntity != null && tileEntity instanceof TileEntityConsumer) {
				return tileEntity;
			}
		}
		return null;
	}

	@Override
	protected boolean transfer(BlockPos destPos, EnumFacing face) {
		TileEntity dest = consumerAt(destPos, face);
		if(dest == null) return false;
		
		/*IFluidHandler sourceCap = source.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
		FluidStack toTransfer = sourceCap.drain(Config.fluidTransfer, false);
		if(toTransfer == null || toTransfer.amount == 0) return false;
		
		IFluidHandler destCap = dest.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
		int transferred = destCap.fill(toTransfer, true);
		XTech.logger.info("drained " + transferred);
		sourceCap.drain(transferred, true);
		return transferred > 0;*/
		return true;
	}

}
