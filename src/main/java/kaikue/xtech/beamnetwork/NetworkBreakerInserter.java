package kaikue.xtech.beamnetwork;

import kaikue.xtech.Config;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkBreakerInserter extends NetworkInserter {

	public NetworkBreakerInserter() {
		super();
	}

	public NetworkBreakerInserter(EnumFacing facing) {
		super(facing);
	}

	@Override
	protected boolean isReceiverAt(World world, BlockPos checkPos, EnumFacing face) {
		IBlockState blockState = world.getBlockState(checkPos);
		Block block = blockState.getBlock();
		boolean isNotAir = !block.isAir(blockState, world, checkPos);
		boolean canMine = block.getHarvestLevel(blockState) <= Config.breakerHarvestLevel;
		return isNotAir && canMine;
	}

	@Override
	protected boolean transfer(World world, BlockPos myPos, BlockPos destPos, EnumFacing face, int reduction) {
		return true;
	}

}
