package kaikue.xtech.tileentities;

import kaikue.xtech.XTech;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityItemInserter extends TileEntity implements ITickable {
	
	@Override
    public void update() {
		
        if (!worldObj.isRemote) {
            XTech.logger.info("Ticking");
        }
    }
	
}
