package kaikue.xtech.blocks;

import kaikue.xtech.Config;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBreakerConsumer extends BlockConsumer {

	public BlockBreakerConsumer() {
		super("breakerconsumer", I18n.format("tooltip.xtech.breaker"), Config.breakerConsumption, Material.IRON, 3.0F, SoundType.METAL);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityBeamNetwork tebn = new TileEntityBeamNetwork("kaikue.xtech.beamnetwork.NetworkBreakerInserter", 
																"kaikue.xtech.beamnetwork.NetworkBreakerConsumer");
		tebn.inserter.facing = EnumFacing.getFront(meta);
		return tebn;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		IBlockState facingState = state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
		worldIn.setBlockState(pos, facingState, 2);
	}
}
