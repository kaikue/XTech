package kaikue.xtech.proxy;

import kaikue.xtech.ModBlocks;
import kaikue.xtech.ModItems;
import kaikue.xtech.ModMisc;
import kaikue.xtech.XTech;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
		ModBlocks.init();
		ModItems.init();
		ModMisc.init();
	}

	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(XTech.instance, new GuiProxy());
	}

}
