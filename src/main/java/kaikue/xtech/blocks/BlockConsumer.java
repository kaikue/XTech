package kaikue.xtech.blocks;

import java.util.Arrays;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockConsumer extends DirectionalBaseBlock implements ITileEntityProvider {

	public BlockConsumer(String name, String description, int consumption, Material material, float hardness, SoundType sound) {
		super(name, 
				Arrays.asList(description, 
							I18n.format("tooltip.xtech.consumption") + ": " + consumption + " " + I18n.format("tooltip.xtech.units")), 
				material, hardness, sound);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		IBlockState facingState = state.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
		worldIn.setBlockState(pos, facingState, 2);
	}
}
