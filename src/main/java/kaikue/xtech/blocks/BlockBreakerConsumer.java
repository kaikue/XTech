package kaikue.xtech.blocks;

import kaikue.xtech.Config;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
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
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer));
	}
}
