package kaikue.xtech.items;

import kaikue.xtech.ModMisc;
import kaikue.xtech.XTech;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseItem extends Item {
	
	public BaseItem(String name, boolean addToCreativeTab) {
		setRegistryName(name);
		setUnlocalizedName(XTech.MODID + "." + name);
		GameRegistry.register(this);
		if (addToCreativeTab) {
			setCreativeTab(ModMisc.creativeTab);
		}
	}

	public BaseItem(String name) {
		this(name, true);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
