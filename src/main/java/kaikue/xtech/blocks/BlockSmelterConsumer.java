package kaikue.xtech.blocks;

import kaikue.xtech.tileentities.TileEntitySmelterConsumer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSmelterConsumer extends BlockConsumer {

	public BlockSmelterConsumer() {
		super("smelterconsumer", "Uses power to smelt items", Material.IRON);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySmelterConsumer(); //EnumFacing.getFront(meta)
	}
}
