package kaikue.xtech;

import kaikue.xtech.render.RenderInserterBeam;
import kaikue.xtech.tileentities.TileEntityFluidInserter;
import kaikue.xtech.tileentities.TileEntityGenerator;
import kaikue.xtech.tileentities.TileEntityItemInserter;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRendering {
	public static void init() {
		ModBlocks.initModels();
		ModItems.initModels();
	}

	public static void registerTESRs() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemInserter.class, 
				new RenderInserterBeam(RenderInserterBeam.Color.BLUE));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidInserter.class, 
				new RenderInserterBeam(RenderInserterBeam.Color.GREEN));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGenerator.class, 
				new RenderInserterBeam(RenderInserterBeam.Color.RED));
	}
}
