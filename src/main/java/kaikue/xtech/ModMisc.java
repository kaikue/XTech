package kaikue.xtech;

import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModMisc {

	public static final CreativeTabs creativeTab = new CreativeTabs(XTech.MODNAME) {
		@Override public Item getTabIconItem() {
			return ModItems.itemLaserCore;
		}
	};

	public static void init() {
		initOreDict();
		registerTileEntities();
		registerWorldGeneration();
	}

	public static void initOreDict() {
		OreDictionary.registerOre("itemSilicon", ModItems.itemSilicon);
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityBeamNetwork.class, XTech.MODID + "_beamnetwork");
	}

	public static void registerWorldGeneration() {
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 4);
	}
}
