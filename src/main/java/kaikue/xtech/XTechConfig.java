package kaikue.xtech;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = XTech.MODID, name = XTech.MODID, category = "")
public class XTechConfig {
	
	public static General general = new General();
	
	@Config.LangKey("xtech.config.general")
	public static class General {
		@Name("Beam Distance")
		@Comment("Maximum distance beams can travel")
		@RangeInt(min = 1, max = 256)
		public int beamDistance = 64;
		
		@Name("Items Transfer")
		@Comment("Number of items to transfer per half-second")
		@RangeInt(min = 1, max = 64)
		public int itemsTransfer = 8;
		
		@Name("Fluid Transfer")
		@Comment("Amount of fluid (in mB) to transfer per half-second")
		@RangeInt(min = 1, max = 100000)
		public int fluidTransfer = 100;
		
		@Name("Breaker Harvest Level")
		@Comment("Harvest level of breaker (obsidian is 3)")
		@RangeInt(min = 0, max = 100)
		public int breakerHarvestLevel = 3;
	}
	
	public static WorldGen worldGen = new WorldGen();
	
	@Config.LangKey("xtech.config.worldgen")
	public static class WorldGen {
		@Name("Generate Silicon")
		@Comment("Whether to generate silicon crystals underground")
		public boolean generateSilicon = true;
		
		@Name("Silicon Chance")
		@Comment("1 in X chance to generate a silicon crystal per chunk (1 = 100% chance)")
		@RangeInt(min = 1, max = 256)
		public int siliconChance = 2;
		
		@Name("Silicon Tries")
		@Comment("Number of attempts to generate silicon in a chunk")
		@RangeInt(min = 1, max = 32)
		public int siliconTries = 8;
	}

	public static Power power = new Power();
	
	public static class Power {
		@Name("Heat Generation")
		@Comment("Power produced by the heated generator")
		@RangeInt(min = 0, max = 1024)
		public int heatGeneration = 32;
		
		@Name("Smelter Consumption")
		@Comment("Power consumed by the smelter")
		@RangeInt(min = 0, max = 1024)
		public int smelterConsumption = 32;
		
		@Name("Breaker Consumption")
		@Comment("Power consumed by the breaker")
		@RangeInt(min = 0, max = 1024)
		public int breakerConsumption = 64;
	}
	
	//From https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/4c25b1020242f2953ba7830e302ce859218d1a23/src/main/java/choonster/testmod3/config/ModConfig.java
	@Mod.EventBusSubscriber
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(XTech.MODID)) {
				ConfigManager.sync(XTech.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
