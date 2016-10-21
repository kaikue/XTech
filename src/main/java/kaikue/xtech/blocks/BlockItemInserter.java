package kaikue.xtech.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockItemInserter extends BaseBlock {

	public BlockItemInserter() {
		super("iteminserter", Material.IRON);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
