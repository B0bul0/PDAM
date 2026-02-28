package me.bobulo.mine.pdam.feature.sign.window;

import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.imgui.ImGuiRenderable;
import me.bobulo.mine.pdam.util.ClipboardUtils;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiHelper.keepInScreen;

/**
 * A GUI window for viewing the text on a Minecraft sign.
 */
public final class SignViewerWindow implements ImGuiRenderable {

    private static long windowCounter = 0;

    public static void openSignEditor(TileEntitySign tileSign) {
        SignViewerWindow signEditorWindow = new SignViewerWindow(tileSign);
        signEditorWindow.initEditor();
    }

    private final String titleId;
    private final ImString signText = new ImString(2048);

    private final ImBoolean isOpen = new ImBoolean();

    private SignViewerWindow(TileEntitySign tileSign) {
        this.titleId = windowCounter++ + "_" + tileSign.getPos().getX() +
          "_" + tileSign.getPos().getY() + "_" + tileSign.getPos().getZ();

        // Extract sign text
        this.signText.set(extractSignText(tileSign));
    }

    @Override
    public void newFrame() {
        if (!isOpen.get()) {
            return;
        }

        setNextWindowSize(300, 160, ImGuiCond.Once);

        float centerX = getIO().getDisplaySizeX() * 0.5f;
        float centerY = getIO().getDisplaySizeY() * 0.5f;
        setNextWindowPos(centerX + 20, centerY - 20, ImGuiCond.Appearing);

        if (begin("Sign Viewer##SignViewerWindow_" + titleId, isOpen, ImGuiWindowFlags.NoResize)) {
            keepInScreen();
            renderContent();
        }

        end();

        if (!isOpen.get()) {
            PDAM.getImGuiRenderer().unregisterWindow(this);
        }
    }

    private void initEditor() {
        PDAM.getImGuiRenderer().registerWindow(this);
        isOpen.set(true);
    }

    private void renderContent() {
        text("Sign Text:");

        inputTextMultiline("##SignMockupTextRead", signText, -1, 70, ImGuiInputTextFlags.ReadOnly);

        if (button("Copy to Clipboard")) {
            ClipboardUtils.copyToClipboard(signText.get());
        }
    }

    private String extractSignText(TileEntitySign tileSign) {
        StringBuilder signText = new StringBuilder();
        for (IChatComponent lineComponent : tileSign.signText) {
            if (lineComponent == null) {
                continue;
            }

            String line = lineComponent.getUnformattedText();
            if (signText.length() > 0) {
                signText.append("\n");
            }

            signText.append(line);
        }

        return signText.toString();
    }

}