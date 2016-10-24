package kaikue.xtech.tileentities;

import kaikue.xtech.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFluidInserter extends TileEntityInserter implements ITickable {

	public TileEntityFluidInserter() {
		super();
	}

	public TileEntityFluidInserter(IBlockState facing) {
		super(facing);
	}

	@Override
	protected boolean isReceiverAt(BlockPos checkPos, EnumFacing face) {
		return tankAt(checkPos, face) != null;
	}

	private TileEntity tankAt(BlockPos checkPos, EnumFacing face) {
		IBlockState blockState = getWorld().getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = getWorld().getTileEntity(checkPos);
			if(tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face)) {
				return tileEntity;
			}
		}
		return null;
	}

	@Override
	protected boolean transfer(BlockPos destPos, EnumFacing face) {
		TileEntity dest = tankAt(destPos, face);
		if(dest == null) return false;
		
		TileEntity source = tankAt(pos.offset(getStateFacing(this.facing).getOpposite()), getStateFacing(this.facing));
		if(source == null) return false;
		
		IFluidHandler sourceCap = source.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getStateFacing(this.facing));
		FluidStack toTransfer = sourceCap.drain(Config.fluidTransfer, true);
		if(toTransfer == null || toTransfer.amount == 0) return false;
		
		IFluidHandler destCap = dest.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
		int transferred = destCap.fill(toTransfer, true);
		if(transferred < Config.fluidTransfer) {
			toTransfer.amount -= transferred;
			sourceCap.fill(toTransfer, true); //this might not work if you can't fill from the extracted side
			if(transferred == 0) return false;
		}
		return true;
	}

}
