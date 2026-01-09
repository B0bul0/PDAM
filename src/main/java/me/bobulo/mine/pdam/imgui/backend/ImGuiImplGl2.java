/*
 * Copyright (C) 2023 moehreag + Contributors
 *
 * This file is part of imguimc and is licensed under the GNU Lesser General
 * Public License v3.0 or later. See LICENSE.LGPL or visit:
 * https://www.gnu.org/licenses/lgpl-3.0.html
 */

package me.bobulo.mine.pdam.imgui.backend;

import java.nio.ByteBuffer;

import imgui.ImDrawData;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec4;
import imgui.type.ImInt;

import static java.lang.Math.toIntExact;
import static org.lwjgl.opengl.GL11.*;

/**
 * ImGui OpenGL 2 backend.
 * From moehreag's <a href="https://github.com/moehreag/imguimc/blob/523af757bb9628b8611cc1076bb0e4f8f88cc162/imgui/src/main/java/ihm/imgui/ImGuiImplGl2.java">ImGuiImplGl2</a>.
 */
public class ImGuiImplGl2 {

    private int fontTexture = -1;

    public void init() {
        ImGuiIO io = ImGui.getIO();
        io.setBackendRendererName("lwjgl2_opengl2");
    }

    public void newFrame() {
        if (fontTexture != -1)
            return;

        // generate font texture
        ImGuiIO io = ImGui.getIO();
        ImInt width = new ImInt();
        ImInt height = new ImInt();
        ByteBuffer image = io.getFonts().getTexDataAsRGBA32(width, height);

        fontTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fontTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
        glTexImage2D(
          GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA,
          GL_UNSIGNED_BYTE, image
        );
        io.getFonts().setTexID(fontTexture);
    }

    private void setupRenderState(int fbWidth, int fbHeight) {
        glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT | GL_TRANSFORM_BIT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_STENCIL_TEST);
        glDisable(GL_LIGHTING);
        glDisable(GL_COLOR_MATERIAL);
        glEnable(GL_SCISSOR_TEST);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glEnable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glShadeModel(GL_SMOOTH);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

        glViewport(0, 0, fbWidth, fbHeight);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, fbWidth, fbHeight, 0, -1.0f, 1.0f);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
    }

    public void renderDrawData(ImDrawData drawData) {
        int fbWidth =
          (int) (drawData.getDisplaySizeX() * drawData.getFramebufferScaleX());
        int fbHeight =
          (int) (drawData.getDisplaySizeY() * drawData.getFramebufferScaleY());

        float clipoffx = drawData.getDisplayPosX();
        float clipoffy = drawData.getDisplayPosY();
        float clipscalex = drawData.getFramebufferScaleX();
        float clipscaley = drawData.getFramebufferScaleY();

        setupRenderState(fbWidth, fbHeight);

        for (int i = 0; i < drawData.getCmdListsCount(); i++) {
            ByteBuffer idxbuffer = drawData.getCmdListIdxBufferData(i);
            // copy buffer to prevent it being overwritten by the next call
            idxbuffer =
              ByteBuffer.allocateDirect(idxbuffer.remaining()).put(idxbuffer);
            ByteBuffer vtxbuffer = drawData.getCmdListVtxBufferData(i);

            int idxsize = ImDrawData.sizeOfImDrawIdx();
            int stride = ImDrawData.sizeOfImDrawVert();

            vtxbuffer.position(0);
            glVertexPointer(2, GL_FLOAT, stride, vtxbuffer);
            vtxbuffer.position(8);
            glTexCoordPointer(2, GL_FLOAT, stride, vtxbuffer);
            vtxbuffer.position(16);
            glColorPointer(4, GL_UNSIGNED_BYTE, stride, vtxbuffer);

            for (int j = 0; j < drawData.getCmdListCmdBufferSize(i); j++) {
                ImVec4 cr = drawData.getCmdListCmdBufferClipRect(i, j);
                float c1x = (cr.x - clipoffx) * clipscalex;
                float c1y = (cr.y - clipoffy) * clipscaley;
                float c2x = (cr.z - clipoffx) * clipscalex;
                float c2y = (cr.w - clipoffy) * clipscaley;
                if (c2x <= c1x || c2y <= c1y)
                    continue;
                glScissor(
                  (int) c1x, (int) (fbHeight - c2y),
                  (int) (c2x - c1x), (int) (c2y - c1y)
                );

                int tex = toIntExact(drawData.getCmdListCmdBufferTextureId(i, j));
                int elemc = drawData.getCmdListCmdBufferElemCount(i, j);
                int idxoff = drawData.getCmdListCmdBufferIdxOffset(i, j);
                int type = idxsize == 2 ? GL_UNSIGNED_SHORT : GL_UNSIGNED_INT;

                glBindTexture(GL_TEXTURE_2D, tex);
                idxbuffer.position(idxoff * idxsize);
                glDrawElements(GL_TRIANGLES, elemc, type, idxbuffer);
            }
        }

        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);

        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glPopAttrib();
    }
}