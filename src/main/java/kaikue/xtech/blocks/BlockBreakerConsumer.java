package kaikue.xtech.blocks;

import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockBreakerConsumer extends BlockGenerator {

	public BlockBreakerConsumer() {
		super("breakerconsumer", I18n.format("tooltip.xtech.breaker"), Material.IRON, 3.0F, SoundType.METAL);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityBeamNetwork tebn = new TileEntityBeamNetwork("kaikue.xtech.beamnetwork.NetworkBreakerInserter", 
																"kaikue.xtech.beamnetwork.NetworkBreakerConsumer");
		tebn.inserter.facing = EnumFacing.getFront(meta);
		return tebn;
	}
}
