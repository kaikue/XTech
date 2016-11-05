package kaikue.xtech.beamnetwork;

import kaikue.xtech.Config;
import net.minecraft.util.EnumFacing;

public class NetworkHeatGenerator extends NetworkGenerator {

	public NetworkHeatGenerator() {
		super(Config.heatGeneration);
	}

	public NetworkHeatGenerator(EnumFacing facing) {
		super(Config.heatGeneration, facing);
	}

}
