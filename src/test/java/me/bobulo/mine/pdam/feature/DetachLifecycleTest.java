package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDetachContext;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDetachContext;
import me.bobulo.mine.pdam.support.RecordingBinding;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class DetachLifecycleTest {
    private MockedStatic<ThreadUtils> threads;
    private MockedStatic<EventUtils> events;
    private final FeatureImpl feature = new FeatureImpl("detach_hooks");

    @BeforeEach
    void simulateGame() {
        threads = mockStatic(ThreadUtils.class);
        threads.when(ThreadUtils::isMainThread).thenReturn(true);
        events = mockStatic(EventUtils.class);
    }

    @AfterEach
    void releaseGame() {
        events.close();
        threads.close();
    }

    @Test
    void ca16DetachEntryPointsCannotBeOverridden() throws NoSuchMethodException {
        assertTrue(Modifier.isFinal(AbstractFeatureModule.class
          .getMethod("onDetach", FeatureModuleDetachContext.class).getModifiers()));
        assertTrue(Modifier.isFinal(AbstractFeatureBehavior.class
          .getMethod("onDetach", FeatureBehaviorDetachContext.class).getModifiers()));
    }

    @Test
    void ca16ModuleHookReceivesTheContextBeforeReleasingTheFeature() {
        DetachModule module = new DetachModule();
        feature.addModule(module);
        feature.enable();

        feature.removeModule(module);

        assertSame(feature, module.featureDuringDetach);
        assertSame(feature, module.context.getFeature());
        assertSame(feature.getLoadScope(), module.context.getScope());
        assertFalse(module.enabledDuringDetach);
        assertNull(module.getFeature());
        assertNull(feature.getModule(DetachModule.class));
    }

    @Test
    void ca16BehaviorHookCanAccessTheFeatureBeforeItIsReleased() {
        DetachBehavior behavior = new DetachBehavior();
        feature.addBehavior(behavior);
        feature.enable();

        feature.removeBehavior(behavior);

        assertSame(feature, behavior.featureDuringDetach);
        assertFalse(behavior.enabledDuringDetach);
        assertNull(behavior.getFeature());
        assertFalse(feature.hasBehavior(DetachBehavior.class));
    }

    @Test
    void ca16FailedModuleHookReleasesTheFeatureAndDoesNotPreventScopeCleanup() {
        DetachModule module = new DetachModule();
        RuntimeException failure = new IllegalStateException("module cleanup failed");
        module.failure = failure;
        RecordingBinding binding = new RecordingBinding("lifetime", new ArrayList<>());
        DetachBehavior behavior = new DetachBehavior();
        feature.addModule(module);
        feature.addBehavior(behavior);
        feature.getLoadScope().registerBinding(binding);

        assertSame(failure, assertThrows(IllegalStateException.class, feature.getLoadScope()::cleanup));

        assertSame(feature, module.featureDuringDetach);
        assertNull(module.getFeature());
        assertNull(behavior.getFeature());
        assertFalse(feature.hasBehavior(DetachBehavior.class));
        assertNull(feature.getModule(DetachModule.class));
        assertEquals(1, binding.getClosures());
    }

    @Test
    void ca16FailedBehaviorHookReleasesTheFeatureAndDoesNotPreventBindingCleanup() {
        DetachBehavior behavior = new DetachBehavior();
        RuntimeException failure = new IllegalStateException("behavior cleanup failed");
        behavior.failure = failure;
        RecordingBinding binding = new RecordingBinding("lifetime", new ArrayList<>());
        feature.addBehavior(behavior);
        feature.getLoadScope().registerBinding(binding);

        assertSame(failure, assertThrows(IllegalStateException.class, feature.getLoadScope()::cleanup));

        assertSame(feature, behavior.featureDuringDetach);
        assertNull(behavior.getFeature());
        assertFalse(feature.hasBehavior(DetachBehavior.class));
        assertEquals(1, binding.getClosures());
    }

    private static final class DetachModule extends AbstractFeatureModule {
        private Feature featureDuringDetach;
        private boolean enabledDuringDetach;
        private FeatureModuleDetachContext context;
        private RuntimeException failure;

        @Override
        protected void onUninitialize(FeatureModuleDetachContext context) {
            this.context = context;
            featureDuringDetach = getFeature();
            enabledDuringDetach = isEnabled();
            if (failure != null) {
                throw failure;
            }
        }
    }

    private static final class DetachBehavior extends AbstractFeatureBehavior {
        private Feature featureDuringDetach;
        private boolean enabledDuringDetach;
        private RuntimeException failure;

        @Override
        protected void onUninitialize(FeatureBehaviorDetachContext context) {
            featureDuringDetach = getFeature();
            enabledDuringDetach = isEnabled();
            if (failure != null) {
                throw failure;
            }
        }
    }
}
