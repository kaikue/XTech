package kaikue.xtech.blocks;

import kaikue.xtech.ModMisc;
import kaikue.xtech.XTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseBlock extends Block {
    public BaseBlock(String name, Material material, boolean addToCreativeTab) {
        super(material);
        setUnlocalizedName(XTech.MODID + "." + name);
        setRegistryName(name);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        if(addToCreativeTab) {
        	setCreativeTab(ModMisc.creativeTab);
        }
    }
    
    public BaseBlock(String name, Material material) {
    	this(name, material, true);
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
