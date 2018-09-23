package kaikue.xtech;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class ModWorldGen implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(world.provider.getDimension() != 0) return;
		if(!XTechConfig.worldGen.generateSilicon || random.nextInt(XTechConfig.worldGen.siliconChance) != 0) return;

		for(int i = 0; i < XTechConfig.worldGen.siliconTries; i++) {
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);
			int y = 1;
			MutableBlockPos pos = new MutableBlockPos(x, y, z);
			IBlockState blockBelow = world.getBlockState(pos);
			while(y < 32 && !world.isAirBlock(pos)) {
				blockBelow = world.getBlockState(pos);
				y++;
				pos.setY(y);
			}
			if(world.isAirBlock(pos) && blockBelow.getBlock().equals(Blocks.STONE)) {
				world.setBlockState(pos, ModBlocks.blockSiliconCrystal.getDefaultState());
				return;
			}
		}
	}

}
