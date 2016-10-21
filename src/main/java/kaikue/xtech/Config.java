package kaikue.xtech;

import org.apache.logging.log4j.Level;

import kaikue.xtech.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {
	//private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_OREGEN = "ore_generation";
    
    public static boolean genCopper = true;

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
            initOregenConfig(cfg);
        } catch (Exception ex) {
            XTech.logger.log(Level.ERROR, "Problem loading config file!", ex);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        //cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
    }

    private static void initOregenConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_OREGEN, "Ore generation configuration");
        String oreComment = "Whether to generate ";
        genCopper = cfg.getBoolean("copper", CATEGORY_OREGEN, genCopper, oreComment + "copper ore");
    }
}
