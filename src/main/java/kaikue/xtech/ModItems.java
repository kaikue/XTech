package kaikue.xtech;

import java.util.ArrayList;
import java.util.Arrays;

import kaikue.xtech.items.BaseItem;
import kaikue.xtech.items.ItemLaserCore;
import kaikue.xtech.items.ItemSilicon;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
	
	public static ArrayList<BaseItem> items;
	
	public static ItemLaserCore itemLaserCore;
	public static ItemSilicon itemSilicon;
	
	public static void init() {
		items = new ArrayList<BaseItem>();
		items.addAll(Arrays.asList(
				itemLaserCore = new ItemLaserCore(),
				itemSilicon = new ItemSilicon()
			));
		
	}
	
	@SideOnly(Side.CLIENT)
    public static void initModels() {
		for(BaseItem item : items) {
			item.initModel();
		}
    }
}
