package kaikue.xtech.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLaserCore extends BaseItem {
	
	public ItemLaserCore() {
		super("lasercore");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, java.util.List<String> info, boolean advanced) 
	{
	    info.add("Description test");
	}
}