package me.bobulo.mine.pdam.feature.hitbox;

import me.bobulo.mine.pdam.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class HitboxesListener {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null || mc.thePlayer == null) return;

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        RenderManager rm = mc.getRenderManager();

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer) {
                continue;
            }

            if (!HitBoxes.SHOW_INVISIBLE.get() && entity.isInvisible()) {
                continue;
            }

            int color = HitBoxes.COLOR.get(); // default color

            if (HitBoxes.ENABLE_ENTITY_TYPES.get()) {
                String entityNameId = EntityList.getEntityString(entity);
                if (entityNameId == null && entity instanceof EntityPlayer) {
                    entityNameId = "Player";
                }

                if (entityNameId != null && !HitBoxes.isEnabledForEntity(entityNameId)) {
                    continue;
                }

                color = HitBoxes.getByEntity(entityNameId).color.get();
            }

            MovingObjectPosition objectMouseOver = mc.objectMouseOver;

            if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && objectMouseOver.entityHit == entity) {
                // red
                color = Color.RED.getRGB();
            } else if (HitBoxes.ONLY_TARGET.get()) {
                continue;
            }

            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks - rm.viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks - rm.viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks - rm.viewerPosZ;

            AxisAlignedBB realBox = entity.getEntityBoundingBox();

            if (HitBoxes.SHOW_EXPANDED.get()) {
                float bordaExtra = entity.getCollisionBorderSize();
                realBox = realBox.expand(bordaExtra, bordaExtra, bordaExtra);
            }

            AxisAlignedBB bbox = new AxisAlignedBB(
              realBox.minX - entity.posX + x,
              realBox.minY - entity.posY + y,
              realBox.minZ - entity.posZ + z,
              realBox.maxX - entity.posX + x,
              realBox.maxY - entity.posY + y,
              realBox.maxZ - entity.posZ + z
            );

            float[] rgba = ColorUtil.toRgba(color);
            GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
            RenderGlobal.drawSelectionBoundingBox(bbox);
        }

        GL11.glDepthMask(true);
        GL11.glPopAttrib();

        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }


}
