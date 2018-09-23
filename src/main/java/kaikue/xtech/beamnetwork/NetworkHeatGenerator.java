package kaikue.xtech.beamnetwork;

import kaikue.xtech.XTechConfig;
import net.minecraft.util.EnumFacing;

public class NetworkHeatGenerator extends NetworkGenerator {

	public NetworkHeatGenerator() {
		super(XTechConfig.power.heatGeneration);
	}

	public NetworkHeatGenerator(EnumFacing facing) {
		super(XTechConfig.power.heatGeneration, facing);
	}

}
