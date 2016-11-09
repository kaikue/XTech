package kaikue.xtech;

import org.apache.logging.log4j.Level;

import kaikue.xtech.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_GENERATION = "worldgeneration";
	private static final String CATEGORY_POWER = "power";

	public static boolean generateSilicon = true;
	public static int siliconChance = 2;
	public static int siliconTries = 8;
	public static int beamDistance = 64;
	public static int fluidTransfer = 100;
	public static int itemsTransfer = 8;
	public static int breakerHarvestLevel = 3;

	public static int heatGeneration = 32;
	public static int smelterConsumption = 64;
	public static int breakerConsumption = 32;

	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
			initWorldGenConfig(cfg);
			initPowerConfig(cfg);
		} catch (Exception ex) {
			XTech.logger.log(Level.ERROR, "Problem loading config file!", ex);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

	private static void initGeneralConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
		beamDistance = cfg.getInt("beamDistance", CATEGORY_GENERAL, beamDistance, 1, 256, "Maximum distance beams can travel");
		itemsTransfer = cfg.getInt("itemsTransfer", CATEGORY_GENERAL, itemsTransfer, 1, 64, "Number of items to transfer per half-second");
		fluidTransfer = cfg.getInt("fluidTransfer", CATEGORY_GENERAL, fluidTransfer, 1, 100000, "Amount of fluid (in mB) to transfer per half-second");
		breakerHarvestLevel = cfg.getInt("breakerHarvestLevel", CATEGORY_GENERAL, breakerHarvestLevel, 0, 20, "Harvest level of breaker (obsidian is 3)");
	}

	private static void initWorldGenConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_GENERATION, "World generation configuration");
		generateSilicon = cfg.getBoolean("generateSilicon", CATEGORY_GENERATION, generateSilicon, "Generate silicon crystals underground");
		siliconChance = cfg.getInt("siliconChance", CATEGORY_GENERATION, siliconChance, 1, 256, "Chance to generate a silicon crystal per chunk (1 = 100% chance)");
		siliconTries = cfg.getInt("siliconTries", CATEGORY_GENERATION, siliconTries, 1, 32, "Number of attempts to generate silicon in a chunk");
	}
	
	private static void initPowerConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_POWER, "Power generation and consumption rates");
		heatGeneration = cfg.getInt("heatGeneration", CATEGORY_POWER, heatGeneration, 0, 1024, "Power produced by the heated generator");
		smelterConsumption = cfg.getInt("smelterConsumption", CATEGORY_POWER, smelterConsumption, 0, 1024, "Power consumed by the smelter");
		breakerConsumption = cfg.getInt("breakerConsumption", CATEGORY_POWER, breakerConsumption, 0, 1024, "Power consumed by the breaker");
	}
}
