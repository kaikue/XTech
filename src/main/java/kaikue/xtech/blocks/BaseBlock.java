package kaikue.xtech.blocks;

import java.util.List;

import kaikue.xtech.ModMisc;
import kaikue.xtech.XTech;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
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

public abstract class BaseBlock extends Block {

	public BaseBlock(String name, List<String> description, Material material, float hardness, SoundType sound, boolean addToCreativeTab) {
		super(material);
		setUnlocalizedName(XTech.MODID + "." + name);
		setHardness(hardness);
		setSoundType(sound);
		setRegistryName(name);
		GameRegistry.register(this);
		ItemBlock itemBlock = getItemBlock(description);
		GameRegistry.register(itemBlock, getRegistryName());
		if(addToCreativeTab) {
			setCreativeTab(ModMisc.creativeTab);
		}
	}

	public BaseBlock(String name, Material material, float hardness, SoundType sound, boolean addToCreativeTab) {
		this(name, null, material, hardness, sound, addToCreativeTab);
	}

	public BaseBlock(String name, List<String> description, Material material, float hardness, SoundType sound) {
		this(name, description, material, hardness, sound, true);
	}

	public BaseBlock(String name, Material material, float hardness, SoundType sound) {
		this(name, null, material, hardness, sound, true);
	}

	private ItemBlock getItemBlock(final List<String> description) {
		if(description == null) return new ItemBlock(this);

		return new ItemBlock(this) {
			@SideOnly(Side.CLIENT)
			@Override
			public void addInformation(ItemStack stack, EntityPlayer player, List<String> info, boolean advanced) {
				for(String line : description) {
					info.add(line);
				}
			}
		};
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
