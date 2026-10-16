package me.bobulo.mine.pdam.support;

import me.bobulo.mine.pdam.binding.Binding;
import me.bobulo.mine.pdam.binding.context.BindingContext;

import java.util.List;

/**
 * A test resource with an observable lifecycle.
 */
public class RecordingBinding implements Binding<String> {

    private final String value;
    private final List<String> calls;
    private BindingContext context;
    private int initializations;
    private int closures;

    public RecordingBinding(String value, List<String> calls) {
        this.value = value;
        this.calls = calls;
    }

    @Override
    public void init(BindingContext context) {
        this.context = context;
        initializations++;
        calls.add("init:" + value);
    }

    @Override
    public String get() {
        return value;
    }

    @Override
    public void close() {
        closures++;
        calls.add("close:" + value);
    }

    public BindingContext getContext() {
        return context;
    }

    public int getInitializations() {
        return initializations;
    }

    public int getClosures() {
        return closures;
    }
}
