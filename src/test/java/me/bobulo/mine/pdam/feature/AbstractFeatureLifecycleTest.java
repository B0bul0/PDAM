package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.behavior.CallbackFeatureBehavior;
import me.bobulo.mine.pdam.feature.context.FeatureDisableContext;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.context.FeatureUninitializeContext;
import me.bobulo.mine.pdam.feature.event.FeatureEnabledEvent;
import me.bobulo.mine.pdam.scope.Scope;
import me.bobulo.mine.pdam.support.RecordingBinding;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.ArgumentMatchers.any;

class AbstractFeatureLifecycleTest {
    private MockedStatic<ThreadUtils> threads;
    private MockedStatic<EventUtils> events;
    private final RecordingFeature feature = new RecordingFeature();
    private final FeatureService service = new FeatureService();

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
    void registrationInitializesBeforeReturningTheFeature() {
        service.registerFeature(feature);

        assertSame(feature, service.getFeature(feature.getId()));
        assertSame(feature, feature.initialization.getFeature());
        assertSame(feature.getLoadScope(), feature.initialization.getScope());
        assertEquals(Arrays.asList("initialize", "init:load"), feature.calls);
        assertFalse(feature.isEnabled());
    }

    @Test
    void automaticEnableWaitsForTheEntireInitializationHook() {
        feature.automatic = true;

        service.registerFeature(feature);

        assertTrue(feature.isEnabled());
        assertTrue(feature.ready);
        assertEquals(Arrays.asList("initialize", "init:load", "enable", "init:enable", "behavior:enable"), feature.calls);
    }

    @Test
    void initializationCanCancelAPendingAutomaticEnable() {
        feature.automatic = true;
        feature.cancelAutomatic = true;

        service.registerFeature(feature);

        assertFalse(feature.isEnabled());
        assertNull(feature.activation);
        assertEquals(Arrays.asList("initialize", "init:load"), feature.calls);
    }

    @Test
    void reenableRenewsTheActivationContextAndPreservesInitialization() {
        service.registerFeature(feature);
        feature.enable();
        FeatureEnableContext previous = feature.activation;
        feature.disable();
        feature.enable();

        assertSame(feature, feature.activation.getFeature());
        assertSame(feature.getEnableScope(), feature.activation.getScope());
        assertNotSame(previous.getScope(), feature.activation.getScope());
        assertSame(feature.getLoadScope(), feature.initialization.getScope());
        assertEquals(1, feature.load.getInitializations());
    }

    @Test
    void disableHookRunsBeforeBehaviorsAndActivationBindingsAreReleased() {
        service.registerFeature(feature);
        feature.enable();
        Scope activation = feature.getEnableScope();
        feature.calls.clear();

        feature.disable();

        assertSame(feature, feature.deactivation.getFeature());
        assertSame(activation, feature.deactivation.getScope());
        assertEquals(Arrays.asList("disable", "behavior:disable", "close:enable"), feature.calls);
        assertEquals(0, feature.load.getClosures());
        assertNull(feature.getEnableScope());
    }

    @Test
    void removalUninitializesBeforeReleasingPersistentResources() {
        service.registerFeature(feature);
        feature.enable();
        feature.calls.clear();

        service.unregisterFeature(feature.getId());

        assertSame(feature, feature.removal.getFeature());
        assertSame(feature.getLoadScope(), feature.removal.getScope());
        assertEquals(Arrays.asList("disable", "behavior:disable", "close:enable", "uninitialize", "close:load"), feature.calls);
        assertNull(service.getFeature(feature.getId()));
    }

    @Test
    void failedInitializationReleasesResourcesAndRemovesTheRegistration() {
        feature.initializeFailure = new IllegalStateException("initialize failed");

        assertSame(feature.initializeFailure,
                assertThrows(IllegalStateException.class, () -> service.registerFeature(feature)));

        assertFalse(service.isFeatureRegistered(feature.getId()));
        assertEquals(1, feature.load.getClosures());
        assertTrue(feature.getLoadScope().getBindings().isEmpty());
        assertEquals(Arrays.asList("initialize", "init:load", "uninitialize", "close:load"), feature.calls);
    }

    @Test
    void failedEnableRunsDisableHookAndPreservesInitializationForRetry() {
        service.registerFeature(feature);
        feature.enableFailure = new IllegalStateException("enable failed");
        feature.calls.clear();

        assertSame(feature.enableFailure, assertThrows(IllegalStateException.class, feature::enable));

        assertEquals(Arrays.asList("enable", "init:enable", "disable", "close:enable"), feature.calls);
        assertFalse(feature.isEnabled());
        assertNull(feature.getEnableScope());
        assertEquals(0, feature.load.getClosures());
        feature.enableFailure = null;
        feature.enable();
        assertTrue(feature.isEnabled());
    }

    @Test
    void failedAutomaticEnableAlsoRollsBackInitializationAndRegistration() {
        feature.automatic = true;
        feature.enableFailure = new IllegalStateException("automatic enable failed");

        assertSame(feature.enableFailure,
                assertThrows(IllegalStateException.class, () -> service.registerFeature(feature)));

        assertFalse(service.isFeatureRegistered(feature.getId()));
        assertEquals(1, feature.load.getClosures());
        assertEquals(1, feature.activationBinding.getClosures());
        assertTrue(feature.getLoadScope().getBehaviorRegistry().getBehaviors().isEmpty());
    }

    @Test
    void failedAutomaticEnableEventAlsoReleasesTheActiveScope() {
        feature.automatic = true;
        IllegalStateException failure = new IllegalStateException("enable event failed");
        events.when(() -> EventUtils.callEvent(any(FeatureEnabledEvent.class))).thenThrow(failure);

        assertSame(failure, assertThrows(IllegalStateException.class, () -> service.registerFeature(feature)));

        assertFalse(service.isFeatureRegistered(feature.getId()));
        assertFalse(feature.isEnabled());
        assertNull(feature.getEnableScope());
        assertEquals(1, feature.load.getClosures());
        assertEquals(1, feature.activationBinding.getClosures());
    }

    @Test
    void disableHookFailureStillDisablesBehaviorsAndReleasesTheScope() {
        service.registerFeature(feature);
        feature.enable();
        feature.disableFailure = new IllegalStateException("disable failed");
        feature.calls.clear();

        assertSame(feature.disableFailure, assertThrows(IllegalStateException.class, feature::disable));

        assertEquals(Arrays.asList("disable", "behavior:disable", "close:enable"), feature.calls);
        assertFalse(feature.isEnabled());
        assertNull(feature.getEnableScope());
        assertEquals(1, feature.activationBinding.getClosures());
    }

    @Test
    void removalAttemptsEveryHookAndCleanupAfterDisableAndUninitializeFailures() {
        service.registerFeature(feature);
        feature.enable();
        feature.disableFailure = new IllegalStateException("disable failed");
        feature.uninitializeFailure = new IllegalStateException("uninitialize failed");

        RuntimeException failure = assertThrows(IllegalStateException.class,
                () -> service.unregisterFeature(feature.getId()));

        assertSame(feature.disableFailure, failure);
        assertSame(feature.uninitializeFailure, failure.getSuppressed()[0]);
        assertFalse(service.isFeatureRegistered(feature.getId()));
        assertEquals(1, feature.load.getClosures());
        assertEquals(1, feature.activationBinding.getClosures());
    }

    @Test
    void repeatedLifecycleCallsDoNotRepeatHooks() {
        service.registerFeature(feature);
        feature.initialize();
        feature.enable();
        feature.enable();
        feature.disable();
        feature.disable();
        feature.uninitialize();
        feature.uninitialize();

        assertEquals(Arrays.asList("initialize", "init:load", "enable", "init:enable", "behavior:enable",
                "disable", "behavior:disable", "close:enable", "uninitialize", "close:load"), feature.calls);
    }

    @Test
    void aRemovedFeatureCanBeInitializedAndRegisteredAgain() {
        service.registerFeature(feature);
        service.unregisterFeature(feature.getId());
        feature.calls.clear();

        service.registerFeature(feature);
        feature.enable();

        assertSame(feature, service.getFeature(feature.getId()));
        assertTrue(feature.isEnabled());
        assertEquals(Arrays.asList("initialize", "init:load", "enable", "init:enable", "behavior:enable"), feature.calls);
    }

    @Test
    void registrationOutsideTheGameThreadLeavesTheRegistryUntouched() {
        threads.when(ThreadUtils::isMainThread).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.registerFeature(feature));

        assertFalse(service.isFeatureRegistered(feature.getId()));
        assertNull(feature.initialization);
    }

    @Test
    void implementedBaseMethodsAreFinalExceptTheFourLifecycleHooks() {
        for (Method method : AbstractFeature.class.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers()) && !method.getName().startsWith("on")) {
                assertTrue(Modifier.isFinal(method.getModifiers()), method.toString());
            }
        }
    }

    @Test
    void featureIdentityIsStoredInAPrivateFinalField() throws NoSuchFieldException {
        Field id = AbstractFeature.class.getDeclaredField("id");

        assertEquals(String.class, id.getType());
        assertTrue(Modifier.isPrivate(id.getModifiers()));
        assertTrue(Modifier.isFinal(id.getModifiers()));
    }

    @Test
    void featureIdentityCannotBeOverridden() throws NoSuchMethodException {
        Method getId = AbstractFeature.class.getDeclaredMethod("getId");

        assertTrue(Modifier.isFinal(getId.getModifiers()));
        assertFalse(Modifier.isAbstract(getId.getModifiers()));
    }

    @Test
    void featureIdentityDoesNotChangeAcrossItsLifecycle() {
        String id = feature.getId();

        service.registerFeature(feature);
        feature.enable();
        feature.disable();
        service.unregisterFeature(id);

        assertSame(id, feature.getId());
        assertEquals("feature_contexts", feature.getId());
    }

    @Test
    void constructionRejectsANullFeatureId() {
        assertThrows(NullPointerException.class, () -> TestFeature.of(null));
    }

    @Test
    void constructionRejectsAnEmptyFeatureId() {
        assertThrows(IllegalArgumentException.class, () -> TestFeature.of(""));
    }

    @Test
    void constructionRejectsABlankFeatureId() {
        assertThrows(IllegalArgumentException.class, () -> TestFeature.of(" \t "));
    }

    private static final class RecordingFeature extends AbstractFeature {
        private final List<String> calls = new ArrayList<>();
        private FeatureInitializeContext initialization;
        private FeatureEnableContext activation;
        private FeatureDisableContext deactivation;
        private FeatureUninitializeContext removal;
        private RecordingBinding load;
        private RecordingBinding activationBinding;
        private boolean ready;
        private boolean automatic;
        private boolean cancelAutomatic;
        private RuntimeException initializeFailure;
        private RuntimeException enableFailure;
        private RuntimeException disableFailure;
        private RuntimeException uninitializeFailure;

        private RecordingFeature() {
            super("feature_contexts");
        }

        @Override
        protected void onInitialize(FeatureInitializeContext context) {
            initialization = context;
            calls.add("initialize");
            load = new RecordingBinding("load", calls);
            context.getScope().registerBinding(load);
            if (automatic) {
                enable();
            }
            if (cancelAutomatic) {
                disable();
            }
            context.getScope().registerBehavior(CallbackFeatureBehavior.of(
                    () -> calls.add("behavior:enable"), () -> calls.add("behavior:disable")));
            ready = true;
            if (initializeFailure != null) {
                throw initializeFailure;
            }
        }

        @Override
        protected void onEnable(FeatureEnableContext context) {
            assertTrue(ready);
            activation = context;
            calls.add("enable");
            activationBinding = new RecordingBinding("enable", calls);
            context.getScope().registerBinding(activationBinding);
            if (enableFailure != null) {
                throw enableFailure;
            }
        }

        @Override
        protected void onDisable(FeatureDisableContext context) {
            deactivation = context;
            assertEquals(0, activationBinding.getClosures());
            calls.add("disable");
            if (disableFailure != null) {
                throw disableFailure;
            }
        }

        @Override
        protected void onUninitialize(FeatureUninitializeContext context) {
            removal = context;
            assertEquals(0, load.getClosures());
            calls.add("uninitialize");
            if (uninitializeFailure != null) {
                throw uninitializeFailure;
            }
        }
    }
}
