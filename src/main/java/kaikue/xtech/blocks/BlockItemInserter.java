package kaikue.xtech.blocks;

import kaikue.xtech.XTech;
import kaikue.xtech.tileentities.TileEntityItemInserter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockItemInserter extends DirectionalBaseBlock implements ITileEntityProvider {
	
	private static final AxisAlignedBB INSERTER_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	
	public BlockItemInserter() {
		super("iteminserter", Material.IRON);
		GameRegistry.registerTileEntity(TileEntityItemInserter.class, XTech.MODID + "_iteminserter");
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return INSERTER_AABB;
        //TODO directional AABBs
    }
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
		//TODO check for inventory?
        return true;
    }
	
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, facing);
    }
	
	public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(meta));
        return iblockstate;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityItemInserter(getStateFromMeta(meta));
	}
}
