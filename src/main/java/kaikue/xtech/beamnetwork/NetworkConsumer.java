package kaikue.xtech.beamnetwork;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class NetworkConsumer {

	private int energy = 0;
	private int energyRequired;

	public NetworkConsumer(int energyRequired) {
		this.energyRequired = energyRequired;
	}

	public void update(World world, BlockPos pos) {
		if(world.isRemote) return;
		if(world.getTotalWorldTime() % 2 == 0) return;

		if(energy >= energyRequired) {
			performOperation(world, pos);
		}

		energy = 0;
	}

	public void addEnergy(int e) {
		energy += e;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", energy);
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		energy = compound.getInteger("energy");
	}

	protected abstract void performOperation(World world, BlockPos pos);

}
