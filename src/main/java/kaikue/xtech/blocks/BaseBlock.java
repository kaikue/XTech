package kaikue.xtech.blocks;

import java.util.List;

import javax.annotation.Nullable;

import kaikue.xtech.ModMisc;
import kaikue.xtech.XTech;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseBlock extends Block {

	public ItemBlock itemBlock;
	
	private List<String> description;
	
	public BaseBlock(String name, List<String> description, Material material, float hardness, SoundType sound, boolean addToCreativeTab) {
		super(material);
		setUnlocalizedName(XTech.MODID + "." + name);
		setHardness(hardness);
		setSoundType(sound);
		setRegistryName(name);
		this.description = description;
		itemBlock = createItemBlock();
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

	private ItemBlock createItemBlock() {
		if(description == null) return new ItemBlock(this);

		return new ItemBlock(this) {
			@SideOnly(Side.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
				for(String line : description) {
					tooltip.add(line);
				}
			}
		};
	}
	
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
