package kaikue.xtech;

import java.util.ArrayList;
import java.util.Arrays;

import kaikue.xtech.blocks.BaseBlock;
import kaikue.xtech.blocks.BlockFluidInserter;
import kaikue.xtech.blocks.BlockItemInserter;
import kaikue.xtech.blocks.BlockMirror;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static ArrayList<BaseBlock> blocks;

	public static BlockMirror blockMirror;
	public static BlockItemInserter blockItemInserter;
	public static BlockFluidInserter blockFluidInserter;

	public static void init() {
		blocks = new ArrayList<BaseBlock>();
		blocks.addAll(Arrays.asList(
				blockMirror = new BlockMirror(),
				blockItemInserter = new BlockItemInserter(),
				blockFluidInserter = new BlockFluidInserter()
				));
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		for(BaseBlock block : blocks) {
			block.initModel();
		}
	}
}
