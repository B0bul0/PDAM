package me.bobulo.mine.pdam.feature.module;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * An interface representing a feature that can have modules added to it.
 */
public interface ModularFeature {

    void addModule(@NotNull FeatureModule module);

    void removeModule(@NotNull FeatureModule module);

    boolean hasModule(@NotNull FeatureModule module);

    boolean hasModule(@NotNull Class<? extends FeatureModule> module);

    <T extends FeatureModule> T getModule(@NotNull Class<T> moduleClass);

    @NotNull Collection<FeatureModule> getModules();

}
