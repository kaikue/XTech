package kaikue.xtech.proxy;

import java.io.File;

import kaikue.xtech.Config;
import kaikue.xtech.ModBlocks;
import kaikue.xtech.ModItems;
import kaikue.xtech.ModMisc;
import kaikue.xtech.ModRecipes;
import kaikue.xtech.XTech;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "xtech.cfg"));
		Config.readConfig();

		ModBlocks.init();
		ModItems.init();
		ModRecipes.init();
		ModMisc.init();
	}

	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(XTech.instance, new GuiProxy());
	}

	public void postInit(FMLPostInitializationEvent e) {
		if(config.hasChanged()) {
			config.save();
		}
	}
}
