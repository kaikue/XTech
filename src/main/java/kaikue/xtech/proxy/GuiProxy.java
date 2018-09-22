package kaikue.xtech.proxy;

import kaikue.xtech.gui.GuiSmelter;
import kaikue.xtech.tileentities.ContainerBeamNetwork;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityBeamNetwork) {
			return new ContainerBeamNetwork(player.inventory, (TileEntityBeamNetwork)te);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityBeamNetwork) {
			TileEntityBeamNetwork containerTileEntity = (TileEntityBeamNetwork)te;
			//TODO different types of gui based on block
			return new GuiSmelter(containerTileEntity, new ContainerBeamNetwork(player.inventory, containerTileEntity));
		}
		return null;
	}
}
