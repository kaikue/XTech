package kaikue.xtech.tileentities;

import kaikue.xtech.Config;
import kaikue.xtech.ModBlocks;
import kaikue.xtech.XTech;
import kaikue.xtech.blocks.DirectionalBaseBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;

public class TileEntityItemInserter extends TileEntity implements ITickable {
	
	private IBlockState facing;
	private BlockPos inventoryPos;
	private int invCheckCooldown;
	
	public TileEntityItemInserter() {
		resetInvCheckCooldown();
	}
	
	public TileEntityItemInserter(IBlockState facing) {
		this();
		this.facing = facing;
	}
	
	@Override
    public void update() {
		if (worldObj.isRemote) {
            return;
        }
		
		boolean foundInventory = true;
        //every few ticks, insert into inventory (if it is there)
        //if it's not there, flag = false
        invCheckCooldown--;
        if(invCheckCooldown < 1 || !foundInventory) {
        	inventoryPos = findClosestInventory();
        	XTech.logger.info("New inv: " + inventoryPos);
        	resetInvCheckCooldown();
        }
    }
	
	private BlockPos findClosestInventory() {
		EnumFacing direction = getStateFacing(facing);
		MutableBlockPos checkPos = new MutableBlockPos(pos);
		for(int i = 0; i < Config.beamDistance; i++) {
			checkPos.move(direction);
			
			//don't check out of bounds
			if(!getWorld().isBlockLoaded(checkPos)) break;
			
			IBlockState blockState = getWorld().getBlockState(checkPos);
			if(blockState.getBlock().hasTileEntity(blockState)) {
	            TileEntity tileEntity = getWorld().getTileEntity(checkPos);
	            if (tileEntity != null && tileEntity instanceof IInventory)
	            {
					return checkPos.toImmutable();
	            }
			}
			
			if(blockState.getBlock() == ModBlocks.blockMirror) {
				direction = turnDirection(direction, getStateFacing(blockState));
				if(direction == null) break; //hit the back of a mirror
			}
			
			if(blockState.isOpaqueCube()) {
				break;
			}
        }
		return null;
	}
	
	private EnumFacing turnDirection(EnumFacing original, EnumFacing mirror) {
		if(mirror == original.getOpposite()) {
			return original.rotateY();
		}
		if(mirror == original.rotateYCCW()) {
			return mirror;
		}
		return null;
	}
	
	private EnumFacing getStateFacing(IBlockState blockState) {
		return blockState.getValue(DirectionalBaseBlock.FACING);
	}
	
	private void resetInvCheckCooldown() {
		invCheckCooldown = 20;
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        compound.setInteger("facing", ModBlocks.blockItemInserter.getMetaFromState(this.facing));
        return compound;
    }
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.facing = ModBlocks.blockItemInserter.getStateFromMeta(compound.getInteger("facing"));
    }
	
}
