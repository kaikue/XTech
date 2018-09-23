package kaikue.xtech.blocks;

import kaikue.xtech.XTechConfig;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockHeatGenerator extends BlockGenerator {

	public BlockHeatGenerator() {
		super("heatgenerator", I18n.format("tooltip.xtech.heatgen"), XTechConfig.power.heatGeneration, Material.IRON, 3.0F, SoundType.METAL);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityBeamNetwork tebn = new TileEntityBeamNetwork("kaikue.xtech.beamnetwork.NetworkHeatGenerator", null);
		tebn.inserter.facing = EnumFacing.getFront(meta);
		return tebn;
	}
}
