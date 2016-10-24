package kaikue.xtech.blocks;

import kaikue.xtech.tileentities.TileEntityFluidInserter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFluidInserter extends BlockInserter implements ITileEntityProvider {

	public BlockFluidInserter() {
		super("fluidinserter", "Transfers fluids into remote tanks", Material.IRON);
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		//TODO check for tank?
		return super.canPlaceBlockAt(worldIn, pos);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFluidInserter(getStateFromMeta(meta));
	}

}
