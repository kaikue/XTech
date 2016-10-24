package kaikue.xtech.blocks;

import kaikue.xtech.tileentities.TileEntityItemInserter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockItemInserter extends BlockInserter implements ITileEntityProvider {

	public BlockItemInserter() {
		super("iteminserter", "Transfers items into remote inventories", Material.IRON);
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		//TODO check for inventory?
		return super.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityItemInserter(getStateFromMeta(meta));
	}

}
