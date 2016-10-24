package kaikue.xtech.util;

import kaikue.xtech.blocks.DirectionalBaseBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class Utils {

	public static int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(DirectionalBaseBlock.FACING)).getIndex();
	}

}
