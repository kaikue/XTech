package kaikue.xtech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class ModMisc {
	
	public static final CreativeTabs creativeTab = new CreativeTabs(XTech.MODNAME) {
	    @Override public Item getTabIconItem() {
	        return ModItems.itemLaserCore;
	    }
	};
	
	public static void init() {
		initOreDict();
	}
	
	public static void initOreDict() {
		OreDictionary.registerOre("itemSilicon", ModItems.itemSilicon);
	}
}
