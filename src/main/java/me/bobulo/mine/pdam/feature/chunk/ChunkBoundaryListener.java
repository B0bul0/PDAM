package me.bobulo.mine.pdam.feature.chunk;

import me.bobulo.mine.pdam.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

/**
 * Listener for rendering chunk boundaries and sections in the Minecraft world.
 */
public final class ChunkBoundaryListener {

    private int minY;
    private int maxY;
    private int playerSectionY;

    private float defaultLineWidth = 1.0F;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        EntityPlayer player = mc.thePlayer;
        RenderManager rm = mc.getRenderManager();

        int playerChunkX = ((int) Math.floor(player.posX) >> 4) << 4;
        int playerChunkZ = ((int) Math.floor(player.posZ) >> 4) << 4;
        int playerY = (int) Math.floor(player.posY);

        this.playerSectionY = ((playerY >> 4) << 4);

        int radius = ChunkViewer.SURROUNDING_RADIUS.get();
        int verticalSections = ChunkViewer.VERTICAL_SECTIONS.get();

        this.minY = Math.max(0, ((playerY >> 4) << 4) - (verticalSections * 16));
        this.maxY = Math.min(256, ((playerY >> 4) << 4) + ((verticalSections + 1) * 16));

        this.defaultLineWidth = ChunkViewer.LINE_WIDTH.getOrDefault(1.0F);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glTranslated(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
        GL11.glLineWidth(defaultLineWidth);

        if (ChunkViewer.SHOW_SURROUNDING.get()) {
            renderSurroundingChunks(playerChunkX, playerChunkZ, radius);
        }

        renderCurrentChunk(playerChunkX, playerChunkZ);
        renderCurrentChunkSection(playerChunkX, playerChunkZ);

        GL11.glDepthMask(true);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderSurroundingChunks(int playerChunkX, int playerChunkZ, int radius) {
        for (int xOffset = -radius; xOffset <= radius; xOffset++) {
            for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                if (xOffset == 0 && zOffset == 0) continue;

                int chunkX = playerChunkX + (xOffset * 16);
                int chunkZ = playerChunkZ + (zOffset * 16);

                renderSurroundingChunk(chunkX, chunkZ);
            }
        }
    }

    private void renderSurroundingChunk(int chunkX, int chunkZ) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i <= 16; i += 16) {
            for (int j = 0; j <= 16; j += 16) {
                float[] color = ColorUtil.toRgba(ChunkViewer.SURROUNDING_COLOR.get());
                worldrenderer.pos(chunkX + i, minY, chunkZ + j).color(color[0], color[1], color[2], color[3]).endVertex();
                worldrenderer.pos(chunkX + i, maxY, chunkZ + j).color(color[0], color[1], color[2], color[3]).endVertex();
            }
        }

        tessellator.draw();
    }

    private void renderCurrentChunk(int chunkX, int chunkZ) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        boolean showSections = ChunkViewer.SHOW_SECTIONS.get();
        boolean showCurrentChunk = ChunkViewer.SHOW_CURRENT_CHUNK.get();

        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        // Render chunk boundaries
        if (showCurrentChunk) {
            for (int i = 0; i <= 16; i += 16) {
                for (int j = 0; j <= 16; j += 16) {
                    float[] color = ColorUtil.toRgba(ChunkViewer.CURRENT_CHUNK_COLOR.get());
                    worldrenderer.pos(chunkX + i, minY, chunkZ + j).color(color[0], color[1], color[2], color[3]).endVertex();
                    worldrenderer.pos(chunkX + i, maxY, chunkZ + j).color(color[0], color[1], color[2], color[3]).endVertex();
                }
            }
        }

        // Render sections

        int chunkSize = 16;
        for (int y = minY; y <= maxY; y += 16) {
            if (!showSections && y % chunkSize == 0) continue;

            if (y == playerSectionY || y == playerSectionY + 16) continue;

            float[] color = ColorUtil.toRgba(ChunkViewer.SECTION_COLOR.get());

            worldrenderer.pos(chunkX, y, chunkZ).color(color[0], color[1], color[2], color[3]).endVertex();
            worldrenderer.pos(chunkX + chunkSize, y, chunkZ).color(color[0], color[1], color[2], color[3]).endVertex();

            worldrenderer.pos(chunkX + chunkSize, y, chunkZ).color(color[0], color[1], color[2], color[3]).endVertex();
            worldrenderer.pos(chunkX + chunkSize, y, chunkZ + chunkSize).color(color[0], color[1], color[2], color[3]).endVertex();

            worldrenderer.pos(chunkX + chunkSize, y, chunkZ + chunkSize).color(color[0], color[1], color[2], color[3]).endVertex();
            worldrenderer.pos(chunkX, y, chunkZ + chunkSize).color(color[0], color[1], color[2], color[3]).endVertex();

            worldrenderer.pos(chunkX, y, chunkZ + chunkSize).color(color[0], color[1], color[2], color[3]).endVertex();
            worldrenderer.pos(chunkX, y, chunkZ).color(color[0], color[1], color[2], color[3]).endVertex();
        }
        tessellator.draw();
    }

    private void renderCurrentChunkSection(int chunkX, int chunkZ) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (ChunkViewer.HIGHLIGHT_CURRENT_SECTION.get()) {
            GL11.glLineWidth(defaultLineWidth * 2); // Line width
            GL11.glDisable(GL11.GL_DEPTH_TEST); // Allow lines to be drawn on top of everything else
        }

        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        int chunkSize = 16;
        float[] color = ColorUtil.toRgba(ChunkViewer.HIGHLIGHT_COLOR.get());

        for (int i = 0; i <= 16; i += 16) {
            for (int j = 0; j <= 16; j += 16) {
                worldrenderer.pos(chunkX + i, playerSectionY, chunkZ + j).color(color[0], color[1], color[2], color[3]).endVertex();
                worldrenderer.pos(chunkX + i, playerSectionY + 16, chunkZ + j).color(color[0], color[1], color[2], color[3]).endVertex();
            }
        }

        for (int y = playerSectionY; y <= playerSectionY + 16; y += 16) {
            worldrenderer.pos(chunkX, y, chunkZ).color(color[0], color[1], color[2], color[3]).endVertex();
            worldrenderer.pos(chunkX + chunkSize, y, chunkZ).color(color[0], color[1], color[2], color[3]).endVertex();

            worldrenderer.pos(chunkX + chunkSize, y, chunkZ).color(color[0], color[1], color[2], color[3]).endVertex();
            worldrenderer.pos(chunkX + chunkSize, y, chunkZ + chunkSize).color(color[0], color[1], color[2], color[3]).endVertex();

            worldrenderer.pos(chunkX + chunkSize, y, chunkZ + chunkSize).color(color[0], color[1], color[2], color[3]).endVertex();
            worldrenderer.pos(chunkX, y, chunkZ + chunkSize).color(color[0], color[1], color[2], color[3]).endVertex();

            worldrenderer.pos(chunkX, y, chunkZ + chunkSize).color(color[0], color[1], color[2], color[3]).endVertex();
            worldrenderer.pos(chunkX, y, chunkZ).color(color[0], color[1], color[2], color[3]).endVertex();
        }

        tessellator.draw();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(defaultLineWidth);
    }
}