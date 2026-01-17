package me.bobulo.mine.pdam.feature.sign.window;

import imgui.ImGuiInputTextCallbackData;
import imgui.callback.ImGuiInputTextCallback;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.imgui.ImGuiRenderable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import static imgui.ImGui.*;
import static me.bobulo.mine.pdam.imgui.util.ImGuiDrawUtil.keepInScreen;

/**
 * A GUI window for editing the text on a Minecraft sign.
 */
public final class SignEditorWindow implements ImGuiRenderable {

    private static final int SIGN_LINES = 4;

    public static void openSignEditor(TileEntitySign tileSign) {
        SignEditorWindow signEditorWindow = new SignEditorWindow(tileSign);
        signEditorWindow.initEditor();
    }

    private final TileEntitySign tileSign;
    private final String titleId;
    private final ImString signText = new ImString(2048);

    private final ImBoolean isEditing = new ImBoolean();

    private SignEditorWindow(TileEntitySign tileSign) {
        this.tileSign = tileSign;
        this.titleId = tileSign.getPos().getX() + "_" + tileSign.getPos().getY() + "_" + tileSign.getPos().getZ();
    }

    @Override
    public void newFrame() {
        if (!isEditing.get()) {
            return;
        }

        setNextWindowSize(300, 160, ImGuiCond.Once);

        float centerX = getIO().getDisplaySizeX() * 0.5f;
        float centerY = getIO().getDisplaySizeY() * 0.5f;
        setNextWindowPos(centerX + 20, centerY - 20, ImGuiCond.Appearing);
//      setNextWindowFocus();

        if (begin("Sign Editor##SignEditorWindow_" + titleId, isEditing)) {
            keepInScreen();
            renderContent();
        }

        end();

        checkExistingSign();

        if (!isEditing.get()) {
            PDAM.getImGuiRenderer().unregisterWidow(this);
            tileSign.setEditable(true);
        }
    }

    private void initEditor() {
        PDAM.getImGuiRenderer().registerWidow(this);
        isEditing.set(true);
        tileSign.setEditable(false);

        // copy existing sign text
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIGN_LINES; i++) {
            if (i > 0) {
                sb.append("\n");
            }

            if (tileSign.signText[i] != null) {
                sb.append(tileSign.signText[i].getUnformattedText());
            }
        }

        signText.set(sb.toString());
    }

    private void endEditor() {
        // send sign update to server
        NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getNetHandler();
        if (netHandlerPlayClient != null) {
            netHandlerPlayClient.addToSendQueue(new C12PacketUpdateSign(tileSign.getPos(), tileSign.signText));
        }

        tileSign.setEditable(true);
        isEditing.set(false);
    }

    private void renderContent() {
        text("Sign Position: " + tileSign.getPos().getX() + ", " + tileSign.getPos().getY() + ", " + tileSign.getPos().getZ());

        text("Edit Sign Text:");

        if (inputTextMultiline("##SignEditorTextInput", signText, -1, 70, ImGuiInputTextFlags.CallbackCharFilter, new ImGuiInputTextCallback() {
            @Override
            public void accept(ImGuiInputTextCallbackData data) {
                char c = (char) data.getEventChar();
                if (c == '\n') {
                    // allow only 4 lines
                    String currentText = signText.get();
                    String[] lines = currentText.split("\n", -1);
                    if (lines.length >= SIGN_LINES) {
                        data.setEventChar(0);
                    }

                    return;
                }

                if (!ChatAllowedCharacters.isAllowedCharacter(c)) {
                    data.setEventChar(0);
                }
            }
        })) {
            String raw = signText.get();
            String[] split = raw.split("\n", -1);

            for (int i = 0; i < SIGN_LINES; i++) {
                String line = (i < split.length) ? split[i] : "";
                tileSign.signText[i] = new ChatComponentText(line);
            }

            updateClientSignVisual();
        }

        if (button("Confirm Changes")) {
            setKeyboardFocusHere(-1);
            endEditor();
        }
    }

    private void checkExistingSign() {
        TileEntitySign updatedTileSign = getUpdatedTileSign();
        if (updatedTileSign == null || updatedTileSign.isInvalid()) {
            isEditing.set(false);
        }
    }

    private void updateClientSignVisual() {
        TileEntitySign updatedTileSign = getUpdatedTileSign();
        if (updatedTileSign == null) {
            return;
        }

        if (updatedTileSign.getIsEditable()) {
            System.arraycopy(tileSign.signText, 0, updatedTileSign.signText, 0, 4);
            updatedTileSign.markDirty();
        }
    }


    private TileEntitySign getUpdatedTileSign() {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.theWorld;
        if (!mc.theWorld.isBlockLoaded(tileSign.getPos())) {
            return null;
        }

        BlockPos pos = tileSign.getPos();
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntitySign) {
            return (TileEntitySign) tileEntity;
        }

        return null;
    }

}