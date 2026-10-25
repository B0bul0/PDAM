package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.scope.Scope;

import java.util.function.Consumer;

/**
 * Test fixture for exercising scopes independently of the concrete game features.
 */
final class TestFeature extends AbstractFeature {
    private Consumer<Scope> initialization = scope -> {};
    private Consumer<Scope> activation = scope -> {};

    private TestFeature(String id) {
        super(id);
    }

    static TestFeature of(String id) {
        return new TestFeature(id);
    }

    static TestFeature initialized(String id, Consumer<Scope> initialization, Consumer<Scope> activation) {
        TestFeature feature = of(id);
        feature.initialization = initialization;
        feature.activation = activation;
        feature.initialize();
        return feature;
    }

    @Override
    protected void onInitialize(FeatureInitializeContext context) {
        initialization.accept(context.getScope());
    }

    @Override
    protected void onEnable(FeatureEnableContext context) {
        activation.accept(context.getScope());
    }
}
