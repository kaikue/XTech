package kaikue.xtech.blocks;

import kaikue.xtech.tileentities.TileEntityHeatGenerator;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockHeatGenerator extends BlockGenerator {

	public BlockHeatGenerator() {
		super("heatgenerator", "Generates power from burnable items", Material.IRON, 3.0F, SoundType.METAL);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityHeatGenerator(EnumFacing.getFront(meta));
	}
}
