package kaikue.xtech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kaikue.xtech.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = XTech.MODID, name = XTech.MODNAME, version = XTech.VERSION, dependencies = "required-after:Forge@[12.18.1.2095,)")
public class XTech {

	public static final String MODID = "xtech";
	public static final String MODNAME = "XTech";
	public static final String VERSION = "0.0.1";

	public static final Logger logger = LogManager.getLogger(XTech.MODID);

	@SidedProxy(clientSide = "kaikue.xtech.proxy.ClientProxy", serverSide = "kaikue.xtech.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static XTech instance = new XTech();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

}
