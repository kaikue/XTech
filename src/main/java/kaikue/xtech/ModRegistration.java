package kaikue.xtech;

import kaikue.xtech.blocks.BaseBlock;
import kaikue.xtech.items.BaseItem;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class ModRegistration {

	@SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
		for(BaseBlock block : ModBlocks.blocks) {
			event.getRegistry().register(block);
		}
		
		ResourceLocation resourceLocation = new ResourceLocation(XTech.MODID, "beamnetwork");
		GameRegistry.registerTileEntity(TileEntityBeamNetwork.class, resourceLocation);
    }
	
	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
		for(BaseItem item : ModItems.items) {
			event.getRegistry().register(item);
		}
		for(BaseBlock block : ModBlocks.blocks) {
			event.getRegistry().register(block.itemBlock.setRegistryName(block.getRegistryName()));
		}
		
		ModMisc.registerOreDictionary();
		
    }
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {

		ModItems.initModels();
		ModBlocks.initModels();
		//ModRendering.registerTESRs();
	}
}
