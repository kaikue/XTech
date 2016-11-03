package kaikue.xtech.tileentities;

import kaikue.xtech.XTech;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityGenerator extends TileEntityInserter implements ITickable {

	private int energyGenerated;

	public TileEntityGenerator(int energyGenerated) {
		super();
		this.energyGenerated = energyGenerated;
	}

	public TileEntityGenerator(int energyGenerated, EnumFacing facing) {
		super(facing);
		this.energyGenerated = energyGenerated;
	}

	@Override
	protected boolean isReceiverAt(BlockPos checkPos, EnumFacing face) {
		return consumerAt(checkPos, face) != null;
	}
	
	@Override
	protected void resetInsertCooldown() {
		insertCooldown = 1;
	}

	@Override
	protected boolean shouldUpdate() {
		return worldObj.getTotalWorldTime() % 2 == 0;
	}

	private TileEntityConsumer consumerAt(BlockPos checkPos, EnumFacing face) {
		IBlockState blockState = getWorld().getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = getWorld().getTileEntity(checkPos);
			if(tileEntity != null && tileEntity instanceof TileEntityConsumer) {
				return (TileEntityConsumer)tileEntity;
			}
		}
		return null;
	}

	@Override
	protected boolean transfer(BlockPos destPos, EnumFacing face, int reduction) {
		TileEntityConsumer dest = consumerAt(destPos, face);
		if(dest == null) return false;

		XTech.logger.info("generating");
		dest.addEnergy(energyGenerated / reduction);
		return true;
	}

}
