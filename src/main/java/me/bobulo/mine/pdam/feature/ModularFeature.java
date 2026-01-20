package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.feature.processor.FeatureProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * An interface representing a feature that can have modules added to it.
 */
public interface ModularFeature {

    void addModule(@NotNull FeatureModule module);

    void removeModule(@NotNull FeatureModule module);

    @NotNull Collection<FeatureModule> getModules();

    void addProcessor(@NotNull FeatureProcessor processor);

    void removeProcessor(@NotNull FeatureProcessor processor);

    <T extends FeatureBehavior> T getBehavior(@NotNull Class<T> behaviorClass);

    <T extends FeatureBehavior> boolean hasBehavior(@NotNull Class<T> behaviorClass);

    <T extends FeatureBehavior> Collection<T> getBehaviors();

    <T extends FeatureBehavior> List<T> getBehaviors(@NotNull Class<T> behaviorClass);

}
