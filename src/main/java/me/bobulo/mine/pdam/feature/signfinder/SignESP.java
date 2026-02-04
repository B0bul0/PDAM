package me.bobulo.mine.pdam.feature.signfinder;

import me.bobulo.mine.pdam.util.BlockPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

public final class SignESP {

    static Set<WorldSign> renderingSigns = new HashSet<>();

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null || renderingSigns.isEmpty()) return;

        RenderManager renderManager = mc.getRenderManager();

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        for (WorldSign sign : renderingSigns) {
                renderSignOutline(sign, renderManager);
        }

        GL11.glDepthMask(true);
        GL11.glPopAttrib();

        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderSignOutline(WorldSign te, RenderManager rm) {
        BlockPosition pos = te.getPosition();

        double x = pos.getX() - rm.viewerPosX;
        double y = pos.getY() - rm.viewerPosY;
        double z = pos.getZ() - rm.viewerPosZ;

        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0); // box size

        GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.5f); // outline color

        RenderGlobal.drawSelectionBoundingBox(bb);
    }
}