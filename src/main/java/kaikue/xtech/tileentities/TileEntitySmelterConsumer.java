package kaikue.xtech.tileentities;

import kaikue.xtech.Config;
import kaikue.xtech.XTech;

public class TileEntitySmelterConsumer extends TileEntityConsumer {

	public TileEntitySmelterConsumer() {
		super(Config.smelterConsumption);
	}

	@Override
	protected void performOperation() {
		XTech.logger.info("Smelted");
	}

}
