package kaikue.xtech.proxy;

import kaikue.xtech.ModRendering;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void init(FMLInitializationEvent e) {
		ModRendering.registerTESRs();
	}
	
}
