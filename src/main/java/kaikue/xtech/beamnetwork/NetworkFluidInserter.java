package kaikue.xtech.beamnetwork;

import kaikue.xtech.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class NetworkFluidInserter extends NetworkInserter {

	public NetworkFluidInserter() {
		super();
	}

	public NetworkFluidInserter(EnumFacing facing) {
		super(facing);
	}

	@Override
	protected boolean isReceiverAt(World world, BlockPos checkPos, EnumFacing face) {
		return tankAt(world, checkPos, face) != null;
	}

	private TileEntity tankAt(World world, BlockPos checkPos, EnumFacing face) {
		IBlockState blockState = world.getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = world.getTileEntity(checkPos);
			if(tileEntity != null && tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face)) {
				return tileEntity;
			}
		}
		return null;
	}

	@Override
	protected boolean transfer(World world, BlockPos myPos, BlockPos destPos, EnumFacing face, int reduction) {
		TileEntity dest = tankAt(world, destPos, face);
		if(dest == null) return false;
		
		TileEntity source = tankAt(world, myPos.offset(facing.getOpposite()), facing);
		if(source == null) return false;
		
		IFluidHandler sourceCap = source.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
		FluidStack toTransfer = sourceCap.drain(Config.fluidTransfer / reduction, false);
		if(toTransfer == null || toTransfer.amount == 0) return false;
		
		IFluidHandler destCap = dest.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
		int transferred = destCap.fill(toTransfer, true);
		sourceCap.drain(transferred, true);
		return transferred > 0;
	}

}
