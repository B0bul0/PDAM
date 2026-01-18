package me.bobulo.mine.pdam.feature.designtools.item;

import com.google.common.collect.ImmutableList;

import java.util.List;

public enum HideFlag {

    ENCHANTS,
    ATTRIBUTES,
    UNBREAKABLE,
    DESTROYS,
    PLACED_ON,
    POTION_EFFECTS;

    public static final List<HideFlag> VALUES = ImmutableList.copyOf(values());

    private final int bit;

    HideFlag() {
        this.bit = 1 << this.ordinal();
    }

    public int getBit() {
        return bit;
    }
}