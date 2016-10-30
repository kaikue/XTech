package kaikue.xtech.blocks;

import java.util.Random;

import kaikue.xtech.ModItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSiliconCrystal extends BaseBlock {

	private static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);

	public BlockSiliconCrystal() {
		super("siliconcrystal", Material.ROCK, 2.0f, SoundType.STONE);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CRYSTAL_AABB;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.itemSilicon;
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(2) + 1;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return random.nextInt(fortune * 2) + quantityDropped(random);
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}
}
