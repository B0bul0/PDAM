package me.bobulo.mine.pdam.feature.imgui;

import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;
import me.bobulo.mine.pdam.shortcut.ImGuiShortcut;
import me.bobulo.mine.pdam.shortcut.Shortcut;
import me.bobulo.mine.pdam.shortcut.ShortcutKey;

public final class ToolbarShortcutMenuImGuiRender extends AbstractFeatureModule implements FeatureConfigImGuiRender {
//TODO

    private ShortcutKey shortcutKey;
    private Shortcut shortcut;
    private ToolbarItemWindow window;

    private ImGuiShortcut imGuiShortcut;

    public ToolbarShortcutMenuImGuiRender(ToolbarItemWindow window) {
        this.window = window;
    }

    @Override
    protected void onInitialize() {
        this.shortcut = Shortcut.of("Open/Close " + window.getMenuName(), getFeature().getId() + ".shortcut." + window.getClass().getSimpleName().toLowerCase(), () -> {
            if (window != null) {
                window.toggleVisible();
            }
        }).register();
        this.imGuiShortcut = new ImGuiShortcut(shortcut);
    }

    @Override
    public void draw() {
        imGuiShortcut.render();
    }

}
