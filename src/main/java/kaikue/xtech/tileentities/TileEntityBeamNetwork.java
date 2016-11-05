package kaikue.xtech.tileentities;

import kaikue.xtech.beamnetwork.NetworkConsumer;
import kaikue.xtech.beamnetwork.NetworkInserter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityBeamNetwork extends TileEntity implements ITickable {

	public NetworkInserter inserter;
	public NetworkConsumer consumer;

	public TileEntityBeamNetwork() {
		super();
	}

	public TileEntityBeamNetwork(NetworkInserter inserter) {
		this.inserter = inserter;
	}

	public TileEntityBeamNetwork(NetworkConsumer consumer) {
		this.consumer = consumer;
	}

	public TileEntityBeamNetwork(NetworkInserter inserter, NetworkConsumer consumer) {
		this.inserter = inserter;
		this.consumer = consumer;
	}

	@Override
	public void update() {
		if(inserter != null) {
			if(inserter.update(worldObj, pos)) {
				updateInWorld();
			}
		}
		if(consumer != null) {
			consumer.update(worldObj, pos);
		}
	}

	private void updateInWorld() {
		if (worldObj != null) {
			IBlockState state = worldObj.getBlockState(getPos());
			worldObj.notifyBlockUpdate(getPos(), state, state, 3);
		}
		markDirty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		if(inserter != null) {
			compound = inserter.writeToNBT(compound);
		}
		if(consumer != null) {
			compound = consumer.writeToNBT(compound);
		}
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(inserter != null) {
			inserter.readFromNBT(compound);
		}
		if(consumer != null) {
			consumer.readFromNBT(compound);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

}
