package kaikue.xtech.blocks;

import javax.annotation.Nullable;

import kaikue.xtech.XTech;
import kaikue.xtech.tileentities.TileEntityItemInserter;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockInserter extends DirectionalBaseBlock {

	private static final AxisAlignedBB INSERTER_UP_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
	private static final AxisAlignedBB INSERTER_DOWN_AABB = new AxisAlignedBB(0.25D, 0.5D, 0.25D, 0.75D, 1.0D, 0.75D);
	private static final AxisAlignedBB INSERTER_NORTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.5D, 0.75D, 0.75D, 1.0D);
	private static final AxisAlignedBB INSERTER_SOUTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.5D);
	private static final AxisAlignedBB INSERTER_EAST_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.5D, 0.75D, 0.75D);
	private static final AxisAlignedBB INSERTER_WEST_AABB = new AxisAlignedBB(0.5D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);

	public BlockInserter(String name, String description, Material material) {
		super(name, description, material);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) {
			return true;
		}
		TileEntityItemInserter inserter = inserterAt(worldIn, pos);
		XTech.logger.info(inserter.facing);
		return true;
	}

	//TODO remove
	private TileEntityItemInserter inserterAt(World worldIn, BlockPos checkPos) {
		IBlockState blockState = worldIn.getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = worldIn.getTileEntity(checkPos);
			if(tileEntity != null && tileEntity instanceof TileEntityItemInserter) {
				return (TileEntityItemInserter)tileEntity;
			}
		}
		return null;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch(state.getValue(FACING)) {
		case UP: return INSERTER_UP_AABB;
		case DOWN: return INSERTER_DOWN_AABB;
		case NORTH: return INSERTER_NORTH_AABB;
		case SOUTH: return INSERTER_SOUTH_AABB;
		case EAST: return INSERTER_EAST_AABB;
		case WEST: return INSERTER_WEST_AABB;
		default: return INSERTER_UP_AABB;
		}
	}

	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, facing);
	}

}
