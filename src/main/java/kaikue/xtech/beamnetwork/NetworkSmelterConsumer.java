package kaikue.xtech.beamnetwork;

import kaikue.xtech.XTechConfig;
import kaikue.xtech.XTech;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkSmelterConsumer extends NetworkConsumer {

	public NetworkSmelterConsumer() {
		super(XTechConfig.power.smelterConsumption);
	}

	@Override
	protected void performOperation(World world, BlockPos pos) {
		XTech.logger.info("Smelted");
	}

}
