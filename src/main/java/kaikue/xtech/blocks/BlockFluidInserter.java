package kaikue.xtech.blocks;

import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidInserter extends BlockInserter implements ITileEntityProvider {

	public BlockFluidInserter() {
		super("fluidinserter", I18n.format("tooltip.xtech.fluidinserter"), Material.CIRCUITS,  0.2f, SoundType.METAL);
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		//TODO check for tank?
		return super.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityBeamNetwork tebn = new TileEntityBeamNetwork("kaikue.xtech.beamnetwork.NetworkFluidInserter", null);
		tebn.inserter.facing = EnumFacing.getFront(meta);
		return tebn;
	}

}
