package kaikue.xtech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kaikue.xtech.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = XTech.MODID, dependencies = "required-after:forge@[14.23.4.2759,)", useMetadata = true)
public class XTech {

	public static final String MODID = "xtech";

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

}
