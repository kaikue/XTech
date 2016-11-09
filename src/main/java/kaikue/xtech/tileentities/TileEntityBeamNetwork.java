package kaikue.xtech.tileentities;

import kaikue.xtech.XTech;
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
	public String inserterClass;
	public String consumerClass;

	public TileEntityBeamNetwork(String inserterClass, String consumerClass) {
		super();
		this.inserterClass = inserterClass;
		this.consumerClass = consumerClass;
		if(inserterClass != null) {
			inserter = instantiateInserter(inserterClass);
		}
		if(consumerClass != null) {
			consumer = instantiateConsumer(consumerClass);
		}
	}

	public TileEntityBeamNetwork() {
		this(null, null);
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
		if(inserterClass != null) {
			compound.setString("inserterClass", inserterClass);
		}
		if(inserter != null) {
			compound = inserter.writeToNBT(compound);
		}
		if(consumerClass != null) {
			compound.setString("consumerClass", consumerClass);
		}
		if(consumer != null) {
			compound = consumer.writeToNBT(compound);
		}
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		inserterClass = compound.getString("inserterClass");
		if(!inserterClass.equals("")) {
			inserter = instantiateInserter(inserterClass);
			inserter.readFromNBT(compound);
		}
		consumerClass = compound.getString("consumerClass");
		if(!consumerClass.equals("")) {
			consumer = instantiateConsumer(consumerClass);
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

	private NetworkInserter instantiateInserter(String className) {
		try {
			return (NetworkInserter) Class.forName(className).newInstance();
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			XTech.logger.error("Invalid inserter class \"" + className + "\" for " + this + " at " + this.pos);
			XTech.logger.error(e.getMessage());
			return null;
		}
	}

	private NetworkConsumer instantiateConsumer(String className) {
		try {
			return (NetworkConsumer) Class.forName(className).newInstance();
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			XTech.logger.error("Invalid consumer class \"" + className + "\" for " + this + " at " + this.pos);
			XTech.logger.error(e.getMessage());
			return null;
		}
	}

}
