package kaikue.xtech.beamnetwork;

import java.util.List;

import kaikue.xtech.Config;
import kaikue.xtech.beamnetwork.NetworkInserter.Receiver;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkBreakerConsumer extends NetworkConsumer {

	public NetworkBreakerConsumer() {
		super(Config.breakerConsumption);
	}

	private static NetworkBreakerInserter breakerInserterAt(World world, BlockPos checkPos) {
		IBlockState blockState = world.getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = world.getTileEntity(checkPos);
			if(tileEntity != null && tileEntity instanceof TileEntityBeamNetwork) {
				NetworkInserter inserter = ((TileEntityBeamNetwork)tileEntity).inserter;
				if(inserter != null && inserter instanceof NetworkBreakerInserter) {
					return (NetworkBreakerInserter)inserter;
				}
			}
		}
		return null;
	}

	@Override
	protected void performOperation(World world, BlockPos pos) {
		NetworkBreakerInserter inserter = breakerInserterAt(world, pos);
		List<Receiver> targets = inserter.receivers;
		for(int i = 0; i < targets.size(); i++) {
			BlockPos target = targets.get(i).pos;
			world.destroyBlock(target, true); //TODO: make this pop out the back
		}
	}

}
