package kaikue.xtech;

import kaikue.xtech.render.RenderInserterBeam;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRendering {
	public static void init() {
		ModBlocks.initModels();
		ModItems.initModels();
	}

	public static void registerTESRs() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamNetwork.class, 
				new RenderInserterBeam());
	}
}
