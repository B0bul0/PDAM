package me.bobulo.mine.pdam.config;

import me.bobulo.mine.pdam.PDAM;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.ArrayUtils;

public class PropertyFactory {

    private final Configuration config;

    public PropertyFactory(Configuration config) {
        this.config = config;
    }

    public <T> Property create(String category, PropertySpec<T> spec) {
        T defaultValue = spec.getDefaultValue();
        String name = spec.getName();
        Property prop;

        if (defaultValue instanceof Boolean) {
            prop = config.get(category, name, (Boolean) defaultValue);
        } else if (defaultValue instanceof boolean[]) {
            prop = config.get(category, name, (boolean[]) defaultValue);
        } else if (defaultValue instanceof Boolean[]) {
            prop = config.get(category, name, ArrayUtils.toPrimitive((Boolean[]) defaultValue));
        } else if (defaultValue instanceof Integer) {
            prop = config.get(category, name, (Integer) defaultValue);
        } else if (defaultValue instanceof int[]) {
            prop = config.get(category, name, (int[]) defaultValue);
        } else if (defaultValue instanceof Integer[]) {
            prop = config.get(category, name, ArrayUtils.toPrimitive((Integer[]) defaultValue));
        } else if (defaultValue instanceof Double) {
            prop = config.get(category, name, (Double) defaultValue);
        } else if (defaultValue instanceof double[]) {
            prop = config.get(category, name, (double[]) defaultValue);
        } else if (defaultValue instanceof Double[]) {
            prop = config.get(category, name, ArrayUtils.toPrimitive((Double[]) defaultValue));
        } else if (defaultValue instanceof String) {
            prop = config.get(category, name, (String) defaultValue);
        } else if (defaultValue instanceof String[]) {
            prop = config.get(category, name, (String[]) defaultValue);
        } else {
            // Fallback
            prop = config.get(category, name, String.valueOf(defaultValue));
        }

        if (spec.getComment() != null) {
            prop.comment = spec.getComment();
        }

        Number minValue = spec.getMinValue();
        if (minValue instanceof Integer) {
            prop.setMinValue((Integer) minValue);
        } else if (minValue instanceof Long) {
            prop.setMinValue((Long) minValue);
        } else if (minValue instanceof Double) {
            prop.setMinValue((Double) minValue);
        } else if (minValue instanceof Float) {
            prop.setMinValue((Float) minValue);
        }

        Number maxValue = spec.getMaxValue();
        if (maxValue instanceof Integer) {
            prop.setMaxValue((Integer) maxValue);
        } else if (maxValue instanceof Long) {
            prop.setMaxValue((Long) maxValue);
        } else if (maxValue instanceof Double) {
            prop.setMaxValue((Double) maxValue);
        } else if (maxValue instanceof Float) {
            prop.setMaxValue((Float) maxValue);
        }

        // Define language key for localization
        prop.setLanguageKey(PDAM.MOD_ID + "." + category + "." + name);

        return prop;
    }

}