package kaikue.xtech.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public class DirectionalBaseBlock extends BaseBlock {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public DirectionalBaseBlock(String name, String description, Material material, boolean addToCreativeTab) {
		super(name, description, material, addToCreativeTab);
	}

	public DirectionalBaseBlock(String name, Material material, boolean addToCreativeTab) {
		super(name, material, addToCreativeTab);
	}

	public DirectionalBaseBlock(String name, String description, Material material) {
		super(name, description, material);
	}

	public DirectionalBaseBlock(String name, Material material) {
		super(name, material);
	}

	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}

	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
}
