package kaikue.xtech.blocks;

import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSmelterConsumer extends BlockConsumer {

	public BlockSmelterConsumer() {
		super("smelterconsumer", I18n.format("tooltip.xtech.smelter"), Material.IRON, 3.0F, SoundType.METAL);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityBeamNetwork(null, "kaikue.xtech.beamnetwork.NetworkSmelterConsumer");
	}
}
