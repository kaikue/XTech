package kaikue.xtech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModMisc {

	public static final CreativeTabs creativeTab = new CreativeTabs(XTech.MODID) {
		@Override public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.itemLaserCore);
		}
	};

	public static void init() {
		registerWorldGeneration();
	}

	public static void registerWorldGeneration() {
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 4);
	}
	
	public static void registerOreDictionary() {
		OreDictionary.registerOre("itemSilicon", ModItems.itemSilicon);
	}
}
