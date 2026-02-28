package me.bobulo.mine.pdam.imgui.util;

import imgui.ImGuiTextFilter;
import imgui.flag.ImGuiKey;
import imgui.type.ImString;

import static imgui.ImGui.*;

/**
 * A text input component with suggestion capabilities using ImGui.
 */
public class TextSuggestionInput {

    private final String label;
    private final String[] suggestions;

    // State
    private final ImString previewBuffer = new ImString(256);
    private final ImGuiTextFilter filter = new ImGuiTextFilter();

    public TextSuggestionInput(String label) {
        this.label = label;
        this.suggestions = new String[0];
    }

    public TextSuggestionInput(String label, String[] suggestions) {
        this.label = label;
        this.suggestions = suggestions;
    }

    /**
     * Draws the text suggestion input component.
     */
    public boolean draw() {
        return draw(suggestions);
    }

    /**
     * Draws the text suggestion input component with the provided suggestions.
     */
    public boolean draw(String[] suggestions) {
        String comboPreviewValue = previewBuffer.get();
        boolean isSelected = false;

        if (beginCombo(label, comboPreviewValue)) {
            setNextItemShortcut(ImGuiKey.ImGuiMod_Ctrl | ImGuiKey.F);

            filter.draw();

            if (isWindowAppearing()) {
                setKeyboardFocusHere(-1);
            }

            separator();

            for (String item : suggestions) {
                if (!filter.passFilter(item)) {
                    continue;
                }

                if (selectable(item)) {
                    isSelected = true;
                    previewBuffer.set(item);
                }

                if (isSelected) {
                    setItemDefaultFocus();
                }
            }

            endCombo();
        }

        return isSelected;
    }

    public String getSelected() {
        return previewBuffer.get();
    }

    public void setSelected(String value) {
        previewBuffer.set(value);
    }

    public void clear() {
        previewBuffer.set("");
    }

}


