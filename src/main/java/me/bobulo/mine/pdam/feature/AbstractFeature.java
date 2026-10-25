package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.attribute.AttributeKey;
import me.bobulo.mine.pdam.feature.context.FeatureDisableContext;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.context.FeatureUninitializeContext;
import me.bobulo.mine.pdam.scope.Scope;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Defines a feature through four lifecycle hooks; scopes and transitions are managed centrally.
 */
public abstract class AbstractFeature implements Feature {

    private final String id;
    private final FeatureController controller = new FeatureController(this);
    private final Map<AttributeKey<?>, Object> attributes = new ConcurrentHashMap<>();

    /**
     * Fixes the feature identity at construction, e.g. {@code super(FEATURE_ID)}.
     */
    protected AbstractFeature(@NotNull String id) {
        Validate.notBlank(id, "Feature id cannot be null or blank");
        this.id = id;
    }

    @Override
    public final @NotNull String getId() {
        return id;
    }

    @Override
    public final @NotNull String getName() {
        return I18n.format("pdam.feature." + getId() + ".name", getId());
    }

    @Override
    public final @NotNull String getDescription() {
        return I18n.format("pdam.feature." + getId() + ".description");
    }

    @Override
    public final @NotNull Scope getLoadScope() {
        return controller.getLoadScope();
    }

    @Override
    public final Scope getEnableScope() {
        return controller.getEnableScope();
    }

    @Override
    public final boolean isEnabled() {
        return controller.isEnabled();
    }

    @Override
    public final void initialize() {
        controller.initialize();
    }

    @Override
    public final void uninitialize() {
        controller.uninitialize();
    }

    @Override
    public final void enable() {
        controller.enable();
    }

    @Override
    public final void disable() {
        controller.disable();
    }

    /**
     * Registers persistent resources, e.g. {@code context.getScope().registerBehavior(menu)}.
     */
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
    }

    /**
     * Releases feature state after disable and before the load scope is cleaned up.
     */
    protected void onUninitialize(@NotNull FeatureUninitializeContext context) {
    }

    /**
     * Registers activation resources, e.g. {@code context.getScope().registerBinding(listener)}.
     */
    protected void onEnable(@NotNull FeatureEnableContext context) {
    }

    /**
     * Releases activation state while its context and bound resources remain available.
     */
    protected void onDisable(@NotNull FeatureDisableContext context) {
    }

    /* Attributes */

    @Override
    public final <T> void set(@NotNull AttributeKey<T> key, T value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T get(@NotNull AttributeKey<T> key) {
        return (T) attributes.get(key);
    }

    @Override
    public final <T> boolean has(@NotNull AttributeKey<T> key) {
        return attributes.containsKey(key);
    }

    @Override
    public final <T> void remove(@NotNull AttributeKey<T> key) {
        attributes.remove(key);
    }
}
