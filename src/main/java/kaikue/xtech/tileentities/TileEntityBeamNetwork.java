package kaikue.xtech.tileentities;

import kaikue.xtech.XTech;
import kaikue.xtech.beamnetwork.NetworkConsumer;
import kaikue.xtech.beamnetwork.NetworkInserter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityBeamNetwork extends TileEntity implements ITickable {

	public NetworkInserter inserter;
	public NetworkConsumer consumer;
	public String inserterClass;
	public String consumerClass;
	public ItemStackHandler itemStackHandler;

	public TileEntityBeamNetwork(String inserterClass, String consumerClass, int invSlots) {
		super();
		this.inserterClass = inserterClass;
		this.consumerClass = consumerClass;
		if(inserterClass != null) {
			inserter = instantiateInserter(inserterClass);
		}
		if(consumerClass != null) {
			consumer = instantiateConsumer(consumerClass);
		}

		if(invSlots > 0) {
			itemStackHandler = new ItemStackHandler(invSlots) {
				@Override
				protected void onContentsChanged(int slot) {
					TileEntityBeamNetwork.this.markDirty();
				}
			};
		}
	}

	public TileEntityBeamNetwork(String inserterClass, String consumerClass) {
		this(inserterClass, consumerClass, 0);
	}

	public TileEntityBeamNetwork() {
		this(null, null);
	}

	@Override
	public void update() {
		if(inserter != null) {
			if(inserter.update(getWorld(), pos)) {
				updateInWorld();
			}
		}
		if(consumer != null) {
			consumer.update(getWorld(), pos);
		}
	}

	private void updateInWorld() {
		if (getWorld() != null) {
			IBlockState state = getWorld().getBlockState(getPos());
			getWorld().notifyBlockUpdate(getPos(), state, state, 3);
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
		if(itemStackHandler != null) {
			compound.setTag("items", itemStackHandler.serializeNBT());
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
		if(compound.hasKey("items") && itemStackHandler != null) {
			itemStackHandler.deserializeNBT((NBTTagCompound)compound.getTag("items"));
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

	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemStackHandler != null;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemStackHandler;
		}
		return super.getCapability(capability, facing);
	}

}
