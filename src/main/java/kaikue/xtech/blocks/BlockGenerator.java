package kaikue.xtech.blocks;

import java.util.Arrays;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockGenerator extends DirectionalBaseBlock implements ITileEntityProvider {

	public BlockGenerator(String name, String description, int generation, Material material, float hardness, SoundType sound) {
		super(name, 
				Arrays.asList(description, 
						I18n.format("tooltip.xtech.generation") + ": " + generation + " " + I18n.format("tooltip.xtech.units")), 
				material, hardness, sound);
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}
}
