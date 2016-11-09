package kaikue.xtech.beamnetwork;

import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class NetworkGenerator extends NetworkInserter {

	private int energyGenerated;

	public NetworkGenerator(int energyGenerated) {
		super();
		this.energyGenerated = energyGenerated;
	}

	public NetworkGenerator(int energyGenerated, EnumFacing facing) {
		super(facing);
		this.energyGenerated = energyGenerated;
	}

	@Override
	protected boolean isReceiverAt(World world, BlockPos checkPos, EnumFacing face) {
		return consumerAt(world, checkPos, face) != null;
	}
	
	@Override
	protected void resetInsertCooldown() {
		insertCooldown = 1;
	}

	@Override
	protected boolean shouldUpdate(World world) {
		return world.getTotalWorldTime() % 2 == 0;
	}

	private NetworkConsumer consumerAt(World world, BlockPos checkPos, EnumFacing face) {
		IBlockState blockState = world.getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = world.getTileEntity(checkPos);
			if(tileEntity != null && tileEntity instanceof TileEntityBeamNetwork) {
				return ((TileEntityBeamNetwork)tileEntity).consumer;
			}
		}
		return null;
	}

	@Override
	protected boolean transfer(World world, BlockPos myPos, BlockPos destPos, EnumFacing face, int reduction) {
		NetworkConsumer dest = consumerAt(world, destPos, face);
		if(dest == null) return false;

		dest.addEnergy(energyGenerated / reduction);
		return true;
	}

}
