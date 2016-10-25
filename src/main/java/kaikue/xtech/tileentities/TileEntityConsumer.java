package kaikue.xtech.tileentities;

import kaikue.xtech.XTech;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class TileEntityConsumer extends TileEntity implements ITickable {

	private int energy = 0;
	private int energyRequired;

	public TileEntityConsumer(int energyRequired) {
		this.energyRequired = energyRequired;
	}

	@Override
	public void update() {
		if(worldObj.isRemote) return;
		if(worldObj.getTotalWorldTime() % 2 == 0) return;

		XTech.logger.info("receiving");

		if(energy >= energyRequired) {
			performOperation();
		}

		energy = 0;
	}

	public void addEnergy(int energy) {
		this.energy += energy;
	}

	protected abstract void performOperation();

}
