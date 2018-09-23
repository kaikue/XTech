package kaikue.xtech;

import java.util.ArrayList;
import java.util.Arrays;

import kaikue.xtech.items.BaseItem;
import kaikue.xtech.items.ItemLaserCore;
import kaikue.xtech.items.ItemSilicon;

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

	public static void initModels() {
		for(BaseItem item : items) {
			item.initModel();
		}
	}
}
