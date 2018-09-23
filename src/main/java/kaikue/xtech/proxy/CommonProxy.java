package kaikue.xtech.proxy;

import java.io.File;

import kaikue.xtech.Config;
import kaikue.xtech.ModBlocks;
import kaikue.xtech.ModItems;
import kaikue.xtech.ModMisc;
import kaikue.xtech.XTech;
import kaikue.xtech.blocks.BaseBlock;
import kaikue.xtech.items.BaseItem;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public static Configuration config;

	public void preInit(FMLPreInitializationEvent e) {
		File directory = e.getModConfigurationDirectory();
		config = new Configuration(new File(directory.getPath(), "xtech.cfg"));
		Config.readConfig();

		ModBlocks.init();
		ModItems.init();
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
	
	@SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
		for(BaseBlock block : ModBlocks.blocks) {
			event.getRegistry().register(block);
		}
		
		ResourceLocation resourceLocation = new ResourceLocation(XTech.MODID + "_beamnetwork");
		GameRegistry.registerTileEntity(TileEntityBeamNetwork.class, resourceLocation);
    }
	
	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
		for(BaseItem item : ModItems.items) {
			event.getRegistry().register(item);
		}
		for(BaseBlock block : ModBlocks.blocks) {
			event.getRegistry().register(block.itemBlock.setRegistryName(block.getRegistryName()));
		}
		
		ModMisc.registerOreDictionary();
    }
	
}
