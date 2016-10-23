package kaikue.xtech;

import kaikue.xtech.render.RenderItemInserterBeam;
import kaikue.xtech.tileentities.TileEntityItemInserter;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRendering {
	public static void init() {
		ModBlocks.initModels();
		ModItems.initModels();
	}

	public static void registerTESRs() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemInserter.class, 
				new RenderItemInserterBeam(RenderItemInserterBeam.Color.BLUE));
	}
}
