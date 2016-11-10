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

public class BlockItemInserter extends BlockInserter implements ITileEntityProvider {

	public BlockItemInserter() {
		super("iteminserter", I18n.format("tooltip.xtech.iteminserter"), Material.CIRCUITS, 0.2f, SoundType.METAL);
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		//TODO check for inventory?
		return super.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityBeamNetwork tebn = new TileEntityBeamNetwork("kaikue.xtech.beamnetwork.NetworkItemInserter", null);
		tebn.inserter.facing = EnumFacing.getFront(meta);
		return tebn;
	}

}
