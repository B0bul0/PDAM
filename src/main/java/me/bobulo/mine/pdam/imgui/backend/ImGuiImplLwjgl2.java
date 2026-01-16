/*
 * Copyright (C) 2023 moehreag + Contributors
 *
 * This file is part of imguimc and is licensed under the GNU Lesser General
 * Public License v3.0 or later. See LICENSE.LGPL or visit:
 * https://www.gnu.org/licenses/lgpl-3.0.html
 */

package me.bobulo.mine.pdam.imgui.backend;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiKey;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * ImGui implementation for LWJGL 2.
 * Based on moehreag's <a href="https://github.com/moehreag/imguimc/blob/523af757bb9628b8611cc1076bb0e4f8f88cc162/imgui/src/main/java/ihm/imgui/ImGuiImplDisplay.java">ImGuiImplDisplay</a>.
 */
public class ImGuiImplLwjgl2 {

    private long time = 0;

    public void init() {
        ImGuiIO io = ImGui.getIO();
        io.setBackendPlatformName("lwjgl2_display");
    }

    public void newFrame() {
        ImGuiIO io = ImGui.getIO();

        // set display size
        float ww = Display.getWidth();
        float wh = Display.getHeight();
        io.setDisplaySize(ww, wh);
        io.setDisplayFramebufferScale(1, 1);

        // set delta
        long nutime = System.currentTimeMillis();
        float delta =
          time > 0 ? (float) (((double) nutime - time) / 1000.0) : 1.0f / 60;

        // prevent failed assert for delta > 0.0f
        io.setDeltaTime((delta > 0.0f) ? delta : 0.01f);
        time = nutime;

        if (isInGame()) {
            // reset mouse position when not in gui to prevent large jumps
            io.setMousePos(-1, -1);
        } else {
            // mouse input
            io.setMousePos(Mouse.getX(), wh - Mouse.getY());
        }

    }

    public void onMouseButton(int button, boolean pressed) {
        ImGuiIO io = ImGui.getIO();
        io.addMouseButtonEvent(button, pressed);
    }

    public void onMouseWheel(int scrollDelta) {
        ImGuiIO io = ImGui.getIO();
        io.setMouseWheel(io.getMouseWheel() + (scrollDelta / 120F));
    }

    public void readKey() {
        if (!ImGui.getIO().getWantCaptureKeyboard() || isInGame()) {
            return;
        }

        while (Keyboard.next()) {
            int key = Keyboard.getEventKey() == 0 ?
              Keyboard.getEventCharacter() : Keyboard.getEventKey();

            onKey(key, Keyboard.getEventKeyState());
        }
    }

    public void onKey(int key, boolean pressed) {
        ImGuiIO io = ImGui.getIO();

        if (key == Keyboard.KEY_LCONTROL || key == Keyboard.KEY_RCONTROL) {
            io.addKeyEvent(ImGuiKey.ModCtrl, pressed);
        }
        if (key == Keyboard.KEY_LSHIFT || key == Keyboard.KEY_RSHIFT) {
            io.addKeyEvent(ImGuiKey.ModShift, pressed);
        }
        if (key == Keyboard.KEY_LMENU || key == Keyboard.KEY_RMENU) {
            io.addKeyEvent(ImGuiKey.ModAlt, pressed);
        }
        if (key == Keyboard.KEY_LMETA || key == Keyboard.KEY_RMETA) {
            io.addKeyEvent(ImGuiKey.ModSuper, pressed);
        }

        io.addKeyEvent(lwjglKeyToImGuiKey(key), pressed);

        if (pressed) {
            io.addInputCharacter(Keyboard.getEventCharacter());
        }
    }

    private int lwjglKeyToImGuiKey(final int lwjglKey) {
        switch (lwjglKey) {
            case Keyboard.KEY_TAB:
                return ImGuiKey.Tab;
            case Keyboard.KEY_LEFT:
                return ImGuiKey.LeftArrow;
            case Keyboard.KEY_RIGHT:
                return ImGuiKey.RightArrow;
            case Keyboard.KEY_UP:
                return ImGuiKey.UpArrow;
            case Keyboard.KEY_DOWN:
                return ImGuiKey.DownArrow;
            case Keyboard.KEY_PRIOR:
                return ImGuiKey.PageUp;
            case Keyboard.KEY_NEXT:
                return ImGuiKey.PageDown;
            case Keyboard.KEY_HOME:
                return ImGuiKey.Home;
            case Keyboard.KEY_END:
                return ImGuiKey.End;
            case Keyboard.KEY_INSERT:
                return ImGuiKey.Insert;
            case Keyboard.KEY_DELETE:
                return ImGuiKey.Delete;
            case Keyboard.KEY_BACK:
                return ImGuiKey.Backspace;
            case Keyboard.KEY_SPACE:
                return ImGuiKey.Space;
            case Keyboard.KEY_RETURN:
                return ImGuiKey.Enter;
            case Keyboard.KEY_ESCAPE:
                return ImGuiKey.Escape;
            case Keyboard.KEY_APOSTROPHE:
                return ImGuiKey.Apostrophe;
            case Keyboard.KEY_COMMA:
                return ImGuiKey.Comma;
            case Keyboard.KEY_MINUS:
                return ImGuiKey.Minus;
            case Keyboard.KEY_PERIOD:
                return ImGuiKey.Period;
            case Keyboard.KEY_SLASH:
                return ImGuiKey.Slash;
            case Keyboard.KEY_SEMICOLON:
                return ImGuiKey.Semicolon;
            case Keyboard.KEY_EQUALS:
                return ImGuiKey.Equal;
            case Keyboard.KEY_LBRACKET:
                return ImGuiKey.LeftBracket;
            case Keyboard.KEY_BACKSLASH:
                return ImGuiKey.Backslash;
            case Keyboard.KEY_RBRACKET:
                return ImGuiKey.RightBracket;
            case Keyboard.KEY_GRAVE:
                return ImGuiKey.GraveAccent;
            case Keyboard.KEY_CAPITAL:
                return ImGuiKey.CapsLock;
            case Keyboard.KEY_SCROLL:
                return ImGuiKey.ScrollLock;
            case Keyboard.KEY_NUMLOCK:
                return ImGuiKey.NumLock;
            case Keyboard.KEY_SYSRQ:
                return ImGuiKey.PrintScreen;
            case Keyboard.KEY_PAUSE:
                return ImGuiKey.Pause;
            case Keyboard.KEY_NUMPAD0:
                return ImGuiKey.Keypad0;
            case Keyboard.KEY_NUMPAD1:
                return ImGuiKey.Keypad1;
            case Keyboard.KEY_NUMPAD2:
                return ImGuiKey.Keypad2;
            case Keyboard.KEY_NUMPAD3:
                return ImGuiKey.Keypad3;
            case Keyboard.KEY_NUMPAD4:
                return ImGuiKey.Keypad4;
            case Keyboard.KEY_NUMPAD5:
                return ImGuiKey.Keypad5;
            case Keyboard.KEY_NUMPAD6:
                return ImGuiKey.Keypad6;
            case Keyboard.KEY_NUMPAD7:
                return ImGuiKey.Keypad7;
            case Keyboard.KEY_NUMPAD8:
                return ImGuiKey.Keypad8;
            case Keyboard.KEY_NUMPAD9:
                return ImGuiKey.Keypad9;
            case Keyboard.KEY_DECIMAL:
                return ImGuiKey.KeypadDecimal;
            case Keyboard.KEY_DIVIDE:
                return ImGuiKey.KeypadDivide;
            case Keyboard.KEY_MULTIPLY:
                return ImGuiKey.KeypadMultiply;
            case Keyboard.KEY_SUBTRACT:
                return ImGuiKey.KeypadSubtract;
            case Keyboard.KEY_ADD:
                return ImGuiKey.KeypadAdd;
            case Keyboard.KEY_NUMPADENTER:
                return ImGuiKey.KeypadEnter;
            case Keyboard.KEY_NUMPADEQUALS:
                return ImGuiKey.KeypadEqual;
            case Keyboard.KEY_LSHIFT:
                return ImGuiKey.LeftShift;
            case Keyboard.KEY_LCONTROL:
                return ImGuiKey.LeftCtrl;
            case Keyboard.KEY_LMENU:
                return ImGuiKey.LeftAlt;
            case Keyboard.KEY_LMETA:
                return ImGuiKey.LeftSuper;
            case Keyboard.KEY_RSHIFT:
                return ImGuiKey.RightShift;
            case Keyboard.KEY_RCONTROL:
                return ImGuiKey.RightCtrl;
            case Keyboard.KEY_RMENU:
                return ImGuiKey.RightAlt;
            case Keyboard.KEY_RMETA:
                return ImGuiKey.RightSuper;
            case Keyboard.KEY_APPS:
                return ImGuiKey.Menu;
            case Keyboard.KEY_0:
                return ImGuiKey._0;
            case Keyboard.KEY_1:
                return ImGuiKey._1;
            case Keyboard.KEY_2:
                return ImGuiKey._2;
            case Keyboard.KEY_3:
                return ImGuiKey._3;
            case Keyboard.KEY_4:
                return ImGuiKey._4;
            case Keyboard.KEY_5:
                return ImGuiKey._5;
            case Keyboard.KEY_6:
                return ImGuiKey._6;
            case Keyboard.KEY_7:
                return ImGuiKey._7;
            case Keyboard.KEY_8:
                return ImGuiKey._8;
            case Keyboard.KEY_9:
                return ImGuiKey._9;
            case Keyboard.KEY_A:
                return ImGuiKey.A;
            case Keyboard.KEY_B:
                return ImGuiKey.B;
            case Keyboard.KEY_C:
                return ImGuiKey.C;
            case Keyboard.KEY_D:
                return ImGuiKey.D;
            case Keyboard.KEY_E:
                return ImGuiKey.E;
            case Keyboard.KEY_F:
                return ImGuiKey.F;
            case Keyboard.KEY_G:
                return ImGuiKey.G;
            case Keyboard.KEY_H:
                return ImGuiKey.H;
            case Keyboard.KEY_I:
                return ImGuiKey.I;
            case Keyboard.KEY_J:
                return ImGuiKey.J;
            case Keyboard.KEY_K:
                return ImGuiKey.K;
            case Keyboard.KEY_L:
                return ImGuiKey.L;
            case Keyboard.KEY_M:
                return ImGuiKey.M;
            case Keyboard.KEY_N:
                return ImGuiKey.N;
            case Keyboard.KEY_O:
                return ImGuiKey.O;
            case Keyboard.KEY_P:
                return ImGuiKey.P;
            case Keyboard.KEY_Q:
                return ImGuiKey.Q;
            case Keyboard.KEY_R:
                return ImGuiKey.R;
            case Keyboard.KEY_S:
                return ImGuiKey.S;
            case Keyboard.KEY_T:
                return ImGuiKey.T;
            case Keyboard.KEY_U:
                return ImGuiKey.U;
            case Keyboard.KEY_V:
                return ImGuiKey.V;
            case Keyboard.KEY_W:
                return ImGuiKey.W;
            case Keyboard.KEY_X:
                return ImGuiKey.X;
            case Keyboard.KEY_Y:
                return ImGuiKey.Y;
            case Keyboard.KEY_Z:
                return ImGuiKey.Z;
            case Keyboard.KEY_F1:
                return ImGuiKey.F1;
            case Keyboard.KEY_F2:
                return ImGuiKey.F2;
            case Keyboard.KEY_F3:
                return ImGuiKey.F3;
            case Keyboard.KEY_F4:
                return ImGuiKey.F4;
            case Keyboard.KEY_F5:
                return ImGuiKey.F5;
            case Keyboard.KEY_F6:
                return ImGuiKey.F6;
            case Keyboard.KEY_F7:
                return ImGuiKey.F7;
            case Keyboard.KEY_F8:
                return ImGuiKey.F8;
            case Keyboard.KEY_F9:
                return ImGuiKey.F9;
            case Keyboard.KEY_F10:
                return ImGuiKey.F10;
            case Keyboard.KEY_F11:
                return ImGuiKey.F11;
            case Keyboard.KEY_F12:
                return ImGuiKey.F12;
            case Keyboard.KEY_F13:
                return ImGuiKey.F13;
            case Keyboard.KEY_F14:
                return ImGuiKey.F14;
            case Keyboard.KEY_F15:
                return ImGuiKey.F15;
            default:
                return ImGuiKey.None;
        }
    }

    public boolean isInGame() {
        return Minecraft.getMinecraft().currentScreen == null;
    }

}