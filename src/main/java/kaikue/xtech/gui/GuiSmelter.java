package kaikue.xtech.gui;

import kaikue.xtech.XTech;
import kaikue.xtech.tileentities.ContainerBeamNetwork;
import kaikue.xtech.tileentities.TileEntityBeamNetwork;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiSmelter extends GuiContainer {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 166;

	private static final ResourceLocation background = new ResourceLocation(XTech.MODID, "textures/gui/smelter.png");

	public GuiSmelter(TileEntityBeamNetwork tileEntity, ContainerBeamNetwork container) {
		super(container);
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
