package kaikue.xtech.tileentities;

import kaikue.xtech.Config;
import net.minecraft.util.EnumFacing;

public class TileEntityHeatGenerator extends TileEntityGenerator {

	public TileEntityHeatGenerator() {
		super(Config.heatGeneration);
	}

	public TileEntityHeatGenerator(EnumFacing facing) {
		super(Config.heatGeneration, facing);
	}

}
