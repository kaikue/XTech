package kaikue.xtech.proxy;

import kaikue.xtech.ModRendering;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		ModRendering.init();
	}
}
