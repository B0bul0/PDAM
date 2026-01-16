package me.bobulo.mine.pdam.imgui.guizmo;

import imgui.extension.imguizmo.ImGuizmo;
import imgui.extension.imguizmo.flag.Operation;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public final class GuizmoImGui {

    public static float[] viewMatrix = new float[16];
    public static float[] projMatrix = new float[16];
    public static boolean captured = false;

    private static final FloatBuffer viewBuf = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer projBuf = BufferUtils.createFloatBuffer(16);

    private static final float[] objectMat = new float[16];

    private static final float[] ignoreTranslation = new float[]{0f, 0f, 0f};
    private static final float[] ignoreScale = new float[]{1f, 1f, 1f};
    private static final float[] ignoreRotation = new float[]{0f, 0f, 0f};

    public static void renderFrame() {
        float width = Minecraft.getMinecraft().displayWidth;
        float height = Minecraft.getMinecraft().displayHeight;

        ImGuizmo.beginFrame();
        ImGuizmo.setOrthographic(false);
        ImGuizmo.setRect(0, 0, width, height);
    }

    static void capture() {
        GL11.glPushMatrix();

        projBuf.clear();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projBuf);
        projBuf.get(projMatrix);

        viewBuf.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, viewBuf);
        viewBuf.get(viewMatrix);

        captured = true;

        GL11.glPopMatrix();
    }

    public static boolean manipulateTranslation(int mode, float[] translation) {
        return manipulate(Operation.TRANSLATE, mode, translation, ignoreRotation, ignoreScale);
    }

    public static boolean manipulate(int operation, int mode, float[] translation, float[] rotation, float[] scale) {
        if (!captured) {
            return false;
        }

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        Minecraft mc = Minecraft.getMinecraft();

        double renderPosX = mc.getRenderManager().viewerPosX;
        double renderPosY = mc.getRenderManager().viewerPosY;
        double renderPosZ = mc.getRenderManager().viewerPosZ;

        float[] relativeTranslation = new float[]{
          (float) (translation[0] - renderPosX),
          (float) (translation[1] - renderPosY),
          (float) (translation[2] - renderPosZ)
        };

        ImGuizmo.recomposeMatrixFromComponents(relativeTranslation, rotation, scale, objectMat);

        ImGuizmo.manipulate(
          GuizmoImGui.viewMatrix,
          GuizmoImGui.projMatrix,
          operation,
          mode,
          objectMat
        );

        boolean changed = ImGuizmo.isUsing();

        if (changed) {
            ImGuizmo.decomposeMatrixToComponents(objectMat, relativeTranslation, rotation, scale);

            translation[0] = (float) (relativeTranslation[0] + renderPosX);
            translation[1] = (float) (relativeTranslation[1] + renderPosY);
            translation[2] = (float) (relativeTranslation[2] + renderPosZ);
        }

        GL11.glPopAttrib();

        return changed;
    }

}