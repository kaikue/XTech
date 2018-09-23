package kaikue.xtech.blocks;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMirror extends BaseBlock {

	public static final PropertyEnum<EnumOrientation> FACING = 
			PropertyEnum.<EnumOrientation>create("facing", EnumOrientation.class);
	private static final AxisAlignedBB MIRROR_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

	public BlockMirror(String name, String description, Material material, float hardness, SoundType sound) {
		super(name, Arrays.asList(description), material, hardness, sound);
	}

	public BlockMirror() {
		this("mirror", I18n.format("tooltip.xtech.mirror"), Material.GLASS, 0.3f, SoundType.GLASS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumOrientation.NORTH));
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return MIRROR_AABB;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		EnumFacing playerFacing = placer.getHorizontalFacing().getOpposite();
		EnumOrientation mirrorFacing = EnumOrientation.fromHorizontalFacing(playerFacing);
		IBlockState facingState = this.getDefaultState().withProperty(FACING, mirrorFacing);
		return facingState;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean powered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
		if(powered) {
			rotate(worldIn, pos);
		}
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		if(worldIn.isRemote) {
			return;
		}
		rotate(worldIn, pos);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) {
			return true;
		}
		EnumOrientation oldFacing = state.getValue(FACING);
		state = state.withProperty(FACING, oldFacing.tilt());
		worldIn.setBlockState(pos, state, 2);
		playAdjustedSound(worldIn, pos);
		return true;
	}

	private void rotate(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		EnumOrientation oldFacing = state.getValue(FACING);
		state = state.withProperty(FACING, oldFacing.rotateY());
		worldIn.setBlockState(pos, state, 2);
		playAdjustedSound(worldIn, pos);
	}

	private void playAdjustedSound(World worldIn, BlockPos pos) {
		worldIn.playSound(null, pos, SoundEvents.BLOCK_GLASS_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	public IBlockState getStateFromMeta(int meta) {
		EnumOrientation facing = EnumOrientation.byMetadata(meta);
		return this.getDefaultState().withProperty(FACING, facing);
	}

	public int getMetaFromState(IBlockState state) {
		return ((EnumOrientation)state.getValue(FACING)).getMetadata();
	}

	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}

	public static enum EnumOrientation implements IStringSerializable {
		EAST(0, "east", EnumFacing.EAST),
		WEST(1, "west", EnumFacing.WEST),
		SOUTH(2, "south", EnumFacing.SOUTH),
		NORTH(3, "north", EnumFacing.NORTH),
		UP_EAST(4, "up_east", EnumFacing.UP),
		UP_WEST(5, "up_west", EnumFacing.UP),
		UP_SOUTH(6, "up_south", EnumFacing.UP),
		UP_NORTH(7, "up_north", EnumFacing.UP),
		DOWN_EAST(8, "down_east", EnumFacing.DOWN),
		DOWN_WEST(9, "down_west", EnumFacing.DOWN),
		DOWN_SOUTH(10, "down_south", EnumFacing.DOWN),
		DOWN_NORTH(11, "down_north", EnumFacing.DOWN);

		private static final BlockMirror.EnumOrientation[] META_LOOKUP = new BlockMirror.EnumOrientation[values().length];
		private final int meta;
		private final String name;
		private final EnumFacing facing;

		private EnumOrientation(int meta, String name, EnumFacing facing) {
			this.meta = meta;
			this.name = name;
			this.facing = facing;
		}

		public int getMetadata() {
			return this.meta;
		}

		public EnumFacing getFacing() {
			return this.facing;
		}

		public String toString() {
			return this.name;
		}

		public static BlockMirror.EnumOrientation byMetadata(int meta) {
			if(meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}
			return META_LOOKUP[meta];
		}

		public static BlockMirror.EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
			switch(clickedSide) {
			case DOWN:
				switch (entityFacing.getAxis()) {
				case X:
					switch(entityFacing.getAxisDirection()) {
					case POSITIVE:
						return DOWN_EAST;
					case NEGATIVE:
						return DOWN_WEST;
					}
				case Z:
					switch(entityFacing.getAxisDirection()) {
					case POSITIVE:
						return DOWN_SOUTH;
					case NEGATIVE:
						return DOWN_NORTH;
					}
				default:
					throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
				}
			case UP:
				switch (entityFacing.getAxis()) {
				case X:
					switch(entityFacing.getAxisDirection()) {
					case POSITIVE:
						return UP_EAST;
					case NEGATIVE:
						return UP_WEST;
					}
				case Z:
					switch(entityFacing.getAxisDirection()) {
					case POSITIVE:
						return UP_SOUTH;
					case NEGATIVE:
						return UP_NORTH;
					}
				default:
					throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
				}

			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
			case EAST:
				return EAST;
			default:
				throw new IllegalArgumentException("Invalid facing: " + clickedSide);
			}
		}

		public static EnumOrientation fromHorizontalFacing(EnumFacing facing) {
			switch(facing) {
			case NORTH:
				return NORTH;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
			case EAST:
				return EAST;
			default:
				throw new IllegalArgumentException("Invalid horizontal facing: " + facing);
			}
		}

		public EnumFacing getHorizontalComponent() {
			switch(this) {
			case NORTH:
			case UP_NORTH:
			case DOWN_NORTH:
				return EnumFacing.NORTH;
			case SOUTH:
			case UP_SOUTH:
			case DOWN_SOUTH:
				return EnumFacing.SOUTH;
			case WEST:
			case UP_WEST:
			case DOWN_WEST:
				return EnumFacing.WEST;
			case EAST:
			case UP_EAST:
			case DOWN_EAST:
				return EnumFacing.EAST;
			default:
				throw new IllegalArgumentException("Invalid orientation: " + this);
			}
		}

		public EnumOrientation rotateY() {
			switch(this) {
			case NORTH:
				return EAST;
			case EAST:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
				return NORTH;
			case UP_NORTH:
				return UP_EAST;
			case UP_EAST:
				return UP_SOUTH;
			case UP_SOUTH:
				return UP_WEST;
			case UP_WEST:
				return UP_NORTH;
			case DOWN_NORTH:
				return DOWN_EAST;
			case DOWN_EAST:
				return DOWN_SOUTH;
			case DOWN_SOUTH:
				return DOWN_WEST;
			case DOWN_WEST:
				return DOWN_NORTH;
			default:
				throw new IllegalArgumentException("Invalid orientation: " + this);
			}
		}

		public EnumOrientation tilt() {
			switch(this) {
			case NORTH:
				return UP_NORTH;
			case EAST:
				return UP_EAST;
			case SOUTH:
				return UP_SOUTH;
			case WEST:
				return UP_WEST;
			case UP_NORTH:
				return DOWN_NORTH;
			case UP_EAST:
				return DOWN_EAST;
			case UP_SOUTH:
				return DOWN_SOUTH;
			case UP_WEST:
				return DOWN_WEST;
			case DOWN_NORTH:
				return NORTH;
			case DOWN_EAST:
				return EAST;
			case DOWN_SOUTH:
				return SOUTH;
			case DOWN_WEST:
				return WEST;
			default:
				throw new IllegalArgumentException("Invalid orientation: " + this);
			}
		}

		public boolean isHorizontal() {
			switch(this) {
			case NORTH:
			case EAST:
			case SOUTH:
			case WEST:
				return true;
			default:
				return false;
			}
		}

		public String getName() {
			return this.name;
		}

		static {
			for(BlockMirror.EnumOrientation orientation : values()) {
				META_LOOKUP[orientation.getMetadata()] = orientation;
			}
		}
	}
}
