package kaikue.xtech.tileentities;

import kaikue.xtech.XTech;

public class TileEntitySmelterConsumer extends TileEntityConsumer {

	public TileEntitySmelterConsumer() {
		super(32);
	}

	@Override
	protected void performOperation() {
		XTech.logger.info("Smelted");
	}

}
