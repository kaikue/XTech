package kaikue.xtech.blocks;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public abstract class DirectionalBaseBlock extends BaseBlock {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public DirectionalBaseBlock(String name, List<String> description, Material material, float hardness, SoundType sound, boolean addToCreativeTab) {
		super(name, description, material, hardness, sound, addToCreativeTab);
	}

	public DirectionalBaseBlock(String name, Material material, float hardness, SoundType sound, boolean addToCreativeTab) {
		super(name, material, hardness, sound, addToCreativeTab);
	}

	public DirectionalBaseBlock(String name, List<String> description, Material material, float hardness, SoundType sound) {
		super(name, description, material, hardness, sound);
	}

	public DirectionalBaseBlock(String name, Material material, float hardness, SoundType sound) {
		super(name, material, hardness, sound);
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
