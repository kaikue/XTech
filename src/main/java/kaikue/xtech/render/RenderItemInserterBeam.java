package kaikue.xtech.render;

import org.lwjgl.opengl.GL11;

import kaikue.xtech.XTech;
import kaikue.xtech.tileentities.TileEntityItemInserter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class RenderItemInserterBeam extends TileEntitySpecialRenderer<TileEntityItemInserter> {

	public static enum Color {
		RED,
		GREEN,
		BLUE
	};

	private Color color;
	
	private static final ResourceLocation blueLaser = new ResourceLocation(XTech.MODID, "textures/effects/laser_blue.png");
	private static final ResourceLocation redLaser = new ResourceLocation(XTech.MODID, "textures/effects/laser_red.png");
	private static final ResourceLocation greenLaser = new ResourceLocation(XTech.MODID, "textures/effects/laser_green.png");

	public RenderItemInserterBeam(Color color) {
		super();
		this.color = color;
	}

	/* Thanks to McJty for the rendering logic (from Deep Resonance and McJtyLib)
	 * @see https://github.com/McJty/DeepResonance/blob/master/src/main/java/mcjty/deepresonance/blocks/laser/LaserRenderer.java
	 * @see https://github.com/McJty/McJtyLib/blob/1.10/src/main/java/mcjty/lib/gui/RenderHelper.java
	 */
	@Override
	public void renderTileEntityAt(TileEntityItemInserter teii, double x, double y, double z, float partialTick, int destroyStage) {
		Tessellator tessellator = Tessellator.getInstance();

		if(teii.inventoryPos == null) return;
		
		
		GlStateManager.pushAttrib();

		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GlStateManager.enableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();
		switch (color) {
		case BLUE: this.bindTexture(blueLaser); break;
		case RED: this.bindTexture(redLaser); break;
		case GREEN: this.bindTexture(greenLaser); break;
		}

		EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
		double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * partialTick;
		double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * partialTick;
		double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * partialTick;
		GlStateManager.translate(-pX, -pY, -pZ);

		Vec3d emitter = new Vec3d(teii.getPos().getX() + 0.5f, teii.getPos().getY() + 0.5f, teii.getPos().getZ() + 0.5f);
		Vec3d receiver = new Vec3d(teii.inventoryPos.getX() + 0.5f, teii.inventoryPos.getY() + 0.5f, teii.inventoryPos.getZ() + 0.5f);
		Vec3d player = new Vec3d((float) pX, (float) pY + p.getEyeHeight(), (float) pZ);

		Vec3d prev = emitter;
		/*for(BlockPos mirrorPos : teii.mirrors) {
			Vec3d mirrorVec = new Vec3d(mirrorPos.getX() + 0.5f, mirrorPos.getY() + 0.5f, mirrorPos.getZ() + 0.5f);
			drawBeam(prev, mirrorVec, player, 0.2f);
			prev = mirrorVec;
		}*/
		drawBeam(prev, receiver, player, 0.2f);

		tessellator.draw();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	public static void drawBeam(Vec3d S, Vec3d E, Vec3d P, float width) {
		Vec3d PS = S.subtract(P);
		Vec3d SE = E.subtract(S);

		Vec3d normal = PS.crossProduct(SE);
		normal = normal.normalize();

		Vec3d half = normal.scale(width);
		Vec3d p1 = S.add(half);
		Vec3d p2 = S.subtract(half);
		Vec3d p3 = E.add(half);
		Vec3d p4 = E.subtract(half);

		drawQuad(Tessellator.getInstance(), p1, p3, p4, p2);
	}

	public static void drawQuad(Tessellator tessellator, Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4) {
		int brightness = 240;
		int b1 = brightness >> 16 & 65535;
		int b2 = brightness & 65535;

		VertexBuffer buffer = tessellator.getBuffer();
		buffer.pos(p1.xCoord, p1.yCoord, p1.zCoord).tex(0.0D, 0.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
		buffer.pos(p2.xCoord, p2.yCoord, p2.zCoord).tex(1.0D, 0.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
		buffer.pos(p3.xCoord, p3.yCoord, p3.zCoord).tex(1.0D, 1.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
		buffer.pos(p4.xCoord, p4.yCoord, p4.zCoord).tex(0.0D, 1.0D).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
	}

}
