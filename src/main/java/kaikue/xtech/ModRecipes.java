package kaikue.xtech;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {
	public static void init() {
		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(ModItems.itemLaserCore), 
				"III",
				"RSG",
				"III",
				'I', "ingotIron",
				'R', "dustRedstone",
				'S', "itemSilicon",
				'G', "blockGlass"
				)
				);
		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(ModBlocks.blockMirror),
				"  G",
				"SIG",
				"  G",
				'S', "itemSilicon",
				'I', "ingotIron",
				'G', "blockGlass"
				)
				);

		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(ModBlocks.blockItemInserter),
				"II ",
				"RSL",
				"II ",
				'I', "ingotIron",
				'R', "dustRedstone",
				'L', ModItems.itemLaserCore,
				'S', "itemSilicon"
				)
				);

		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(ModBlocks.blockFluidInserter),
				"II ",
				"RBL",
				"II ",
				'I', "ingotIron",
				'R', "dustRedstone",
				'L', ModItems.itemLaserCore,
				'B', Items.BUCKET
				)
				);
	}
}
