package me.bobulo.mine.devmod.feature.component;

public abstract class AbstractFeatureComponent implements FeatureComponent {

    private boolean enabled = false;

    @Override
    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void enable() {
        if (!enabled) {
            this.onEnable();
            enabled = true;
        }
    }

    @Override
    public final void disable() {
        if (enabled) {
            this.onDisable();
            enabled = false;
        }
    }

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();

}
