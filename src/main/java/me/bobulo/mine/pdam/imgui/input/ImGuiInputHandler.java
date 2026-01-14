package me.bobulo.mine.pdam.imgui.input;

import imgui.ImGui;
import me.bobulo.mine.pdam.imgui.backend.ImGuiImplLwjgl2;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class ImGuiInputHandler {

    private final ImGuiImplLwjgl2 display;

    public ImGuiInputHandler(ImGuiImplLwjgl2 display) {
        this.display = display;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiMouseClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (Mouse.getEventDWheel() != 0) {
            display.onMouseWheel(Mouse.getEventDWheel());
        }

        if (Mouse.getEventButton() != -1) {
            display.onMouseButton(Mouse.getEventButton(), Mouse.getEventButtonState());
        }

        if (isMouseOverImGui()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        int key = Keyboard.getEventKey() == 0 ?
          Keyboard.getEventCharacter() : Keyboard.getEventKey();

        display.onKey(key, Keyboard.getEventKeyState());
    }

    public boolean isMouseOverImGui() {
        return ImGui.getIO().getWantCaptureMouse();
    }

}
