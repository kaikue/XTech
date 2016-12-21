package kaikue.xtech.beamnetwork;

import java.util.ArrayList;
import java.util.List;

import kaikue.xtech.Config;
import kaikue.xtech.beamnetwork.NetworkInserter.Receiver;
import kaikue.xtech.blocks.BlockBreakerConsumer;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class NetworkBreakerConsumer extends NetworkConsumer {

	public NetworkBreakerConsumer() {
		super(Config.breakerConsumption);
	}

	private static NetworkBreakerInserter breakerInserterAt(World world, BlockPos checkPos) {
		IBlockState blockState = world.getBlockState(checkPos);
		if(blockState.getBlock().hasTileEntity(blockState)) {
			TileEntity tileEntity = world.getTileEntity(checkPos);
			if(tileEntity != null && tileEntity instanceof TileEntityBeamNetwork) {
				NetworkInserter inserter = ((TileEntityBeamNetwork)tileEntity).inserter;
				if(inserter != null && inserter instanceof NetworkBreakerInserter) {
					return (NetworkBreakerInserter)inserter;
				}
			}
		}
		return null;
	}

	@Override
	protected void performOperation(World world, BlockPos pos) {
		EnumFacing facing = world.getBlockState(pos).getValue(BlockBreakerConsumer.FACING);
		BlockPos behind = pos.offset(facing.getOpposite());
		List<EnumFacing> facings = new ArrayList<EnumFacing>();
		for(EnumFacing f : EnumFacing.values()) {
			if(f != facing) facings.add(f);
		}
		NetworkBreakerInserter inserter = breakerInserterAt(world, pos);
		List<Receiver> targets = inserter.receivers;
		for(int i = 0; i < targets.size(); i++) {
			BlockPos target = targets.get(i).pos;
			world.destroyBlock(target, true);
			AxisAlignedBB aabb = new AxisAlignedBB(target);
			List<EntityItem> drops = world.getEntitiesWithinAABB(EntityItem.class, aabb);
			for(EntityItem drop : drops) {
				drop.setVelocity(0, 0, 0);
				drop.setPosition(behind.getX() + 0.5, behind.getY() + 0.5, behind.getZ() + 0.5);
				//update drop on client somehow?
			}
			for(EnumFacing face : facings) {
				EnumFacing invFace = face.getOpposite();
				BlockPos sidePos = pos.offset(face.getOpposite());
				TileEntity dest = NetworkItemInserter.inventoryAt(world, sidePos, invFace);
				if(dest != null) {
					IItemHandler destHandler = dest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, invFace);
					for(EntityItem drop : drops) {
						ItemStack stack = drop.getEntityItem();
						for (int j = 0; j < destHandler.getSlots(); j++) {
							ItemStack remainder = destHandler.insertItem(j, stack, false);
							drop.setEntityItemStack(remainder);
							if(remainder == null || remainder.stackSize == 0) {
								drop.setDead();
								break;
							}
						}
					}
				}
			}
		}
	}

}
