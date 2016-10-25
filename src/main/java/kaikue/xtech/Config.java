package kaikue.xtech;

import org.apache.logging.log4j.Level;

import kaikue.xtech.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_POWER = "power";

	public static int beamDistance = 64;
	public static int fluidTransfer = 100;
	
	public static int heatGeneration = 32;
	public static int smelterConsumption = 64;

	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
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
		fluidTransfer = cfg.getInt("fluidTransfer", CATEGORY_GENERAL, fluidTransfer, 1, 100000, "Amount of fluid (in mB) to transfer per half-second");
	}
	
	private static void initPowerConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_POWER, "Power generation and consumption rates");
		heatGeneration = cfg.getInt("heatGeneration", CATEGORY_POWER, heatGeneration, 0, 1024, "Power produced by the heated generator");
		smelterConsumption = cfg.getInt("smelterConsumption", CATEGORY_POWER, smelterConsumption, 0, 1024, "Power consumed by the smelter");
	}
}
