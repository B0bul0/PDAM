package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.component.FeatureComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * An interface representing a feature that can have components added to it.
 */
public interface ComponentableFeature {

    void addComponent(@NotNull FeatureComponent component);

    void removeComponent(@NotNull FeatureComponent component);

    boolean hasComponent(@NotNull FeatureComponent component);

    boolean hasComponent(@NotNull Class<? extends FeatureComponent> component);

    <T extends FeatureComponent> T getComponent(@NotNull Class<T> componentClass);

    @NotNull Collection<FeatureComponent> getComponents();

}
