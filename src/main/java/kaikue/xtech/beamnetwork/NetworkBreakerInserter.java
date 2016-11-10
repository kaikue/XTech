package kaikue.xtech.beamnetwork;

import kaikue.xtech.Config;
import kaikue.xtech.blocks.BlockMirror;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
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
		boolean isNotMirror = !(block instanceof BlockMirror);
		boolean canMine = block.getHarvestLevel(blockState) <= Config.breakerHarvestLevel;
		return isNotAir && isNotMirror && canMine;
	}

	private static NetworkBreakerConsumer breakerConsumerAt(World world, BlockPos checkPos) {
		IBlockState blockState = world.getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = world.getTileEntity(checkPos);
			if(tileEntity != null && tileEntity instanceof TileEntityBeamNetwork) {
				NetworkConsumer consumer = ((TileEntityBeamNetwork)tileEntity).consumer;
				if(consumer != null && consumer instanceof NetworkBreakerConsumer) {
					return (NetworkBreakerConsumer)consumer;
				}
			}
		}
		return null;
	}

	@Override
	protected boolean transfer(World world, BlockPos myPos, BlockPos destPos, EnumFacing face, int reduction) {
		return breakerConsumerAt(world, myPos).hasEnoughPower();
	}

}
