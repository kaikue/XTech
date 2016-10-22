package kaikue.xtech.blocks;

import kaikue.xtech.ModMisc;
import kaikue.xtech.XTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseBlock extends Block {
    public BaseBlock(String name, String description, Material material, boolean addToCreativeTab) {
        super(material);
        setUnlocalizedName(XTech.MODID + "." + name);
        setRegistryName(name);
        GameRegistry.register(this);
        ItemBlock itemBlock = getItemBlock(description);
        GameRegistry.register(itemBlock, getRegistryName());
        if(addToCreativeTab) {
        	setCreativeTab(ModMisc.creativeTab);
        }
    }
    
    public BaseBlock(String name, Material material, boolean addToCreativeTab) {
    	this(name, null, material, addToCreativeTab);
    }
    
    public BaseBlock(String name, String description, Material material) {
    	this(name, description, material, true);
    }
    
    public BaseBlock(String name, Material material) {
    	this(name, null, material, true);
    }
    
    private ItemBlock getItemBlock(final String description) {
    	if(description == null) return new ItemBlock(this);
    	
    	return new ItemBlock(this) {
        	@SideOnly(Side.CLIENT)
        	@Override
        	public void addInformation(ItemStack stack, EntityPlayer player, java.util.List<String> info, boolean advanced) 
        	{
        	    info.add(description);
        	}
        };
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
