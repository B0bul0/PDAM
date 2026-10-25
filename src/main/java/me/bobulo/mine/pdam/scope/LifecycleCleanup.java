package me.bobulo.mine.pdam.scope;

/**
 * Completes every cleanup action and propagates failures with their suppressed causes.
 */
public final class LifecycleCleanup {
    private LifecycleCleanup() {
    }

    /**
     * Runs all actions, for example run(first::close, second::close).
     */
    public static void run(Runnable... actions) {
        RuntimeException failure = null;
        for (Runnable action : actions) {
            try {
                action.run();
            } catch (RuntimeException exception) {
                if (failure == null) {
                    failure = exception;
                } else if (failure != exception) {
                    failure.addSuppressed(exception);
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
    }
}
