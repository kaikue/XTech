package kaikue.xtech;

import java.util.ArrayList;
import java.util.Arrays;

import kaikue.xtech.blocks.BaseBlock;
import kaikue.xtech.blocks.BlockBreakerConsumer;
import kaikue.xtech.blocks.BlockFluidInserter;
import kaikue.xtech.blocks.BlockHeatGenerator;
import kaikue.xtech.blocks.BlockItemInserter;
import kaikue.xtech.blocks.BlockMirror;
import kaikue.xtech.blocks.BlockSiliconCrystal;
import kaikue.xtech.blocks.BlockSmelterConsumer;
import kaikue.xtech.blocks.BlockSplitter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

	public static ArrayList<BaseBlock> blocks;

	public static BlockSiliconCrystal blockSiliconCrystal;
	public static BlockMirror blockMirror;
	public static BlockSplitter blockSplitter;
	public static BlockItemInserter blockItemInserter;
	public static BlockFluidInserter blockFluidInserter;
	public static BlockHeatGenerator blockHeatGenerator;
	public static BlockSmelterConsumer blockSmelterConsumer;
	public static BlockBreakerConsumer blockBreakerConsumer;

	public static void init() {
		blocks = new ArrayList<BaseBlock>();
		blocks.addAll(Arrays.asList(
				blockSiliconCrystal = new BlockSiliconCrystal(),
				blockMirror = new BlockMirror(),
				blockSplitter = new BlockSplitter(),
				blockItemInserter = new BlockItemInserter(),
				blockFluidInserter = new BlockFluidInserter(),
				blockHeatGenerator = new BlockHeatGenerator(),
				blockSmelterConsumer = new BlockSmelterConsumer(),
				blockBreakerConsumer = new BlockBreakerConsumer()
				));
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		for(BaseBlock block : blocks) {
			block.initModel();
		}
	}
}
