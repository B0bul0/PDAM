package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.CallbackFeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorAttachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDetachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDetachContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleDisableContext;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleEnableContext;
import me.bobulo.mine.pdam.scope.Scope;
import me.bobulo.mine.pdam.support.RecordingBinding;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class ScopeRegistriesTest {
    private MockedStatic<ThreadUtils> threads;
    private MockedStatic<EventUtils> events;
    private final TestFeature feature = TestFeature.of("registries");
    private final List<String> calls = new ArrayList<>();

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
    void ca08ModuleAndBehaviorHaveIndependentContracts() {
        assertFalse(FeatureBehavior.class.isAssignableFrom(FeatureModule.class));
        assertFalse(FeatureModule.class.isAssignableFrom(FeatureBehavior.class));
        assertFalse(FeatureModule.class.isAssignableFrom(AbstractFeatureBehavior.class));
    }

    @Test
    void ca08ModuleIsOptionalAndIndexedByItsExactType() {
        assertNull(feature.getModule(UniqueModule.class));
        UniqueModule module = new UniqueModule();
        feature.getLoadScope().registerModule(module);
        assertSame(module, feature.getModule(UniqueModule.class));
        assertSame(module, feature.getLoadScope().getModuleRegistry().getModule(UniqueModule.class));
        assertNull(feature.getModule(AbstractFeatureModule.class));
    }

    @Test
    void ca08DuplicateModuleTypeIsRejectedWithoutReplacingTheOriginal() {
        UniqueModule module = new UniqueModule();
        feature.addModule(module);
        assertThrows(IllegalArgumentException.class, () -> feature.addModule(new UniqueModule()));
        assertSame(module, feature.getModule(UniqueModule.class));
        assertEquals(1, feature.getModules().size());
    }

    @Test
    void ca08UniqueModuleCannotBeDuplicatedAcrossScopes() {
        feature.addModule(new UniqueModule());
        feature.enable();
        assertThrows(IllegalArgumentException.class,
          () -> feature.getEnableScope().registerModule(new UniqueModule()));
    }

    @Test
    void ca08RemovingAModuleAllowsAnotherInstanceOfTheType() {
        UniqueModule first = new UniqueModule();
        feature.addModule(first);
        feature.removeModule(first);
        UniqueModule replacement = new UniqueModule();
        feature.addModule(replacement);
        assertNull(first.getFeature());
        assertSame(replacement, feature.getModule(UniqueModule.class));
        assertThrows(IllegalArgumentException.class, () -> feature.removeModule(first));
    }

    @Test
    void ca09SameBehaviorClassCanBeRegisteredMultipleTimesAndQueriedByInterface() {
        RecordingBehavior first = new RecordingBehavior("first", calls);
        RecordingBehavior second = new RecordingBehavior("second", calls);
        feature.addBehavior(first);
        feature.addBehavior(second);
        assertEquals(Arrays.asList(first, second), feature.getBehavior(RecordingBehavior.class));
        assertEquals(Arrays.asList(first, second), feature.getBehaviors(FeatureBehavior.class));
        assertTrue(feature.hasBehavior(RecordingBehavior.class));
    }

    @Test
    void ca09RepeatedInstanceIsRejectedAndRemovalKeepsItsSibling() {
        RecordingBehavior first = new RecordingBehavior("first", calls);
        RecordingBehavior second = new RecordingBehavior("second", calls);
        feature.addBehavior(first);
        feature.addBehavior(second);
        assertThrows(IllegalArgumentException.class, () -> feature.addBehavior(first));
        feature.removeBehavior(first);
        assertEquals(Collections.singletonList(second), feature.getBehavior(RecordingBehavior.class));
        assertNull(first.getFeature());
        assertThrows(IllegalArgumentException.class, () -> feature.removeBehavior(first));
    }

    @Test
    void ca09BehaviorCallbacksRunOncePerActivationAndBeforeDetach() {
        RecordingBehavior behavior = new RecordingBehavior("first", calls);
        feature.addBehavior(behavior);
        feature.enable();
        feature.enable();
        feature.disable();
        feature.disable();
        feature.removeBehavior(behavior);
        assertEquals(Arrays.asList("attach:first", "enable:first", "disable:first", "detach:first"), calls);
    }

    @Test
    void ca09EnableScopeBehaviorsAreVisibleAndDetachedDuringDisable() {
        feature.enable();
        RecordingBehavior behavior = new RecordingBehavior("temporary", calls);
        Scope activation = feature.getEnableScope();
        activation.registerBehavior(behavior);
        assertEquals(Collections.singletonList(behavior), feature.getBehavior(RecordingBehavior.class));
        feature.disable();
        assertNull(behavior.getFeature());
        assertTrue(activation.getBehaviorRegistry().getBehaviors().isEmpty());
        assertFalse(feature.hasBehavior(RecordingBehavior.class));
        assertEquals(Arrays.asList("attach:temporary", "enable:temporary", "disable:temporary", "detach:temporary"), calls);
    }

    @Test
    void ca09ServiceAggregatesAllBehaviorInstances() {
        FeatureService service = new FeatureService();
        feature.addBehavior(new RecordingBehavior("first", calls));
        feature.addBehavior(new RecordingBehavior("second", calls));
        service.registerFeature(feature);
        assertEquals(2, service.getAllBehaviors(RecordingBehavior.class).size());
        service.unregisterAll();
        assertTrue(feature.getBehavior(RecordingBehavior.class).isEmpty());
    }

    @Test
    void ca10FeatureReinitializesBindingsInFreshScopesOnEveryEnable() {
        RecordingBinding binding = new RecordingBinding("listener", calls);
        TestFeature built = TestFeature.initialized("bound", scope -> {}, scope -> scope.registerBinding(binding));
        assertEquals(0, binding.getInitializations());
        built.enable();
        Scope previous = built.getEnableScope();
        assertEquals("listener", previous.get(String.class));
        built.disable();
        built.enable();
        assertNotSame(previous, built.getEnableScope());
        assertEquals(2, binding.getInitializations());
        assertEquals(1, binding.getClosures());
    }

    @Test
    void ca10BindingsAreReadyBeforeAutomaticEnableAndBehaviorActivation() {
        RecordingBinding binding = new RecordingBinding("listener", calls);
        TestFeature built = TestFeature.initialized("ready",
          scope -> scope.registerBehavior(CallbackFeatureBehavior.enableOnly(
            context -> assertEquals("listener", context.getScope().get(String.class)))),
          scope -> scope.registerBinding(binding));
        built.enable();
        assertEquals(1, binding.getInitializations());
    }

    @Test
    void ca11DisableStillReleasesBindingsAfterACallbackFails() {
        IllegalStateException failure = new IllegalStateException("disable failed");
        feature.addBehavior(CallbackFeatureBehavior.of(() -> {}, () -> {throw failure;}));
        feature.enable();
        RecordingBinding binding = new RecordingBinding("resource", calls);
        feature.getEnableScope().registerBinding(binding);
        assertSame(failure, assertThrows(IllegalStateException.class, feature::disable));
        assertEquals(1, binding.getClosures());
        assertNull(feature.getEnableScope());
        assertFalse(feature.isEnabled());
    }

    @Test
    void ca11FailedActivationReleasesItsScopeAndCanBeRetried() {
        IllegalStateException failure = new IllegalStateException("enable failed");
        RecordingBinding binding = new RecordingBinding("resource", calls);
        TestFeature built = TestFeature.initialized("failed",
          scope -> scope.registerBehavior(CallbackFeatureBehavior.enableOnly(() -> {throw failure;})),
          scope -> scope.registerBinding(binding));
        assertSame(failure, assertThrows(IllegalStateException.class, built::enable));
        assertFalse(built.isEnabled());
        assertNull(built.getEnableScope());
        assertEquals(1, binding.getClosures());
    }

    @Test
    void ca11RegistriesRejectFeatureChangesOutsideTheGameThread() {
        threads.when(ThreadUtils::isMainThread).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> feature.addModule(new UniqueModule()));
        assertThrows(IllegalArgumentException.class,
          () -> feature.addBehavior(new RecordingBehavior("async", calls)));
    }


    @Test
    void ca09SameBehaviorInstanceCannotBelongToBothScopes() {
        RecordingBehavior behavior = new RecordingBehavior("shared", calls);
        feature.addBehavior(behavior);
        feature.enable();
        assertThrows(IllegalArgumentException.class, () -> feature.getEnableScope().registerBehavior(behavior));
        assertSame(feature, behavior.getFeature());
    }

    @Test
    void ca09DistinctEqualBehaviorInstancesAreRemovedByIdentity() {
        EqualBehavior first = new EqualBehavior();
        EqualBehavior second = new EqualBehavior();
        feature.addBehavior(first);
        feature.addBehavior(second);
        feature.removeBehavior(second);
        assertSame(first, feature.getBehavior(EqualBehavior.class).get(0));
        assertEquals(1, feature.getBehavior(EqualBehavior.class).size());
        assertSame(feature, first.getFeature());
        assertNull(second.getFeature());
    }

    @Test
    void ca11FailedBehaviorAdditionDisablesAndDetachesTheFailedInstance() {
        feature.enable();
        IllegalStateException failure = new IllegalStateException("addition failed");
        FailingEnableBehavior behavior = new FailingEnableBehavior(failure, calls);
        assertSame(failure, assertThrows(IllegalStateException.class, () -> feature.addBehavior(behavior)));
        assertNull(behavior.getFeature());
        assertFalse(feature.hasBehavior(FailingEnableBehavior.class));
        assertEquals(Arrays.asList("enable", "disable", "detach"), calls);
    }

    @Test
    void ca11FailedModuleAdditionDisablesAndDetachesTheFailedInstance() {
        feature.enable();
        IllegalStateException failure = new IllegalStateException("addition failed");
        FailingEnableModule module = new FailingEnableModule(failure, calls);
        assertSame(failure, assertThrows(IllegalStateException.class, () -> feature.addModule(module)));
        assertNull(module.getFeature());
        assertNull(feature.getModule(FailingEnableModule.class));
        assertEquals(Arrays.asList("enable", "disable", "detach"), calls);
    }

    @Test
    void ca11FailedAttachmentCanBeRetriedWithoutRetainingAnOwner() {
        IllegalStateException failure = new IllegalStateException("attachment failed");
        FailingAttachBehavior behavior = new FailingAttachBehavior(failure);
        assertSame(failure, assertThrows(IllegalStateException.class, () -> feature.addBehavior(behavior)));
        assertNull(behavior.getFeature());
        feature.addBehavior(behavior);
        assertSame(feature, behavior.getFeature());
        assertEquals(1, feature.getBehavior(FailingAttachBehavior.class).size());
    }

    @Test
    void ca11SelfRemovalDuringActivationDoesNotSkipTheNextBehavior() {
        SelfRemovingBehavior first = new SelfRemovingBehavior(calls);
        RecordingBehavior second = new RecordingBehavior("second", calls);
        feature.addBehavior(first);
        feature.addBehavior(second);
        calls.clear();
        feature.enable();
        assertNull(first.getFeature());
        assertEquals(Arrays.asList("self", "enable:second"), calls);
        assertTrue(second.isEnabled());
    }

    @Test
    void ca11InitializationFailureCleansEarlierRegistrations() {
        UniqueModule first = new UniqueModule();
        IllegalStateException failure = new IllegalStateException("builder failed");
        FailingAttachBehavior second = new FailingAttachBehavior(failure);
        assertSame(failure, assertThrows(IllegalStateException.class,
          () -> TestFeature.initialized("initialization", scope -> {
              scope.registerModule(first);
              scope.registerBehavior(second);
          }, scope -> {})));
        assertNull(first.getFeature());
        assertNull(second.getFeature());
    }

    @Test
    void ca11ServiceRemovalStillDetachesAndClosesAfterDisableFailure() {
        FeatureService service = new FeatureService();
        IllegalStateException failure = new IllegalStateException("service disable failed");
        CallbackFeatureBehavior behavior = CallbackFeatureBehavior.disableOnly(() -> {
            throw failure;
        });
        feature.addBehavior(behavior);
        RecordingBinding binding = new RecordingBinding("lifetime", calls);
        feature.getLoadScope().registerBinding(binding);
        service.registerFeature(feature);
        feature.enable();
        assertSame(failure, assertThrows(IllegalStateException.class, () -> service.unregisterFeature(feature.getId())));
        assertNull(service.getFeature(feature.getId()));
        assertNull(behavior.getFeature());
        assertEquals(1, binding.getClosures());
    }

    @Test
    void ca11CleanupFromTheWrongThreadLeavesRegistrationsAndResourcesIntact() {
        FeatureService service = new FeatureService();
        RecordingBinding binding = new RecordingBinding("lifetime", calls);
        feature.getLoadScope().registerBinding(binding);
        service.registerFeature(feature);
        threads.when(ThreadUtils::isMainThread).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.unregisterFeature(feature.getId()));
        assertThrows(IllegalArgumentException.class, feature.getLoadScope()::cleanup);
        assertSame(feature, service.getFeature(feature.getId()));
        assertEquals(0, binding.getClosures());
    }

    @Test
    void ca08DistinctModuleTypesWithEqualInstancesAreRemovedByIdentity() {
        EqualModule first = new EqualModule();
        OtherEqualModule second = new OtherEqualModule();
        feature.addModule(first);
        feature.addModule(second);
        feature.removeModule(second);
        assertSame(first, feature.getModules().iterator().next());
        assertSame(first, feature.getModule(EqualModule.class));
        assertNull(feature.getModule(OtherEqualModule.class));
        assertSame(feature, first.getFeature());
        assertNull(second.getFeature());
    }

    private static class EqualModule extends AbstractFeatureModule {
        @Override
        public boolean equals(Object other) {
            return other instanceof EqualModule;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    private static final class OtherEqualModule extends EqualModule {}

    private static final class EqualBehavior extends AbstractFeatureBehavior {
        @Override
        public boolean equals(Object other) {
            return other instanceof EqualBehavior;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    private static final class FailingEnableBehavior extends AbstractFeatureBehavior {
        private final RuntimeException failure;
        private final List<String> calls;

        FailingEnableBehavior(RuntimeException failure, List<String> calls) {
            this.failure = failure;
            this.calls = calls;
        }

        @Override
        protected void onEnable(FeatureBehaviorEnableContext context) {
            calls.add("enable");
            throw failure;
        }

        @Override
        protected void onDisable(FeatureBehaviorDisableContext context) {
            calls.add("disable");
        }

        @Override
        protected void onUninitialize(FeatureBehaviorDetachContext context) {
            calls.add("detach");
        }
    }

    private static final class FailingEnableModule extends AbstractFeatureModule {
        private final RuntimeException failure;
        private final List<String> calls;

        FailingEnableModule(RuntimeException failure, List<String> calls) {
            this.failure = failure;
            this.calls = calls;
        }

        @Override
        protected void onEnable(FeatureModuleEnableContext context) {
            calls.add("enable");
            throw failure;
        }

        @Override
        protected void onDisable(FeatureModuleDisableContext context) {
            calls.add("disable");
        }

        @Override
        protected void onUninitialize(FeatureModuleDetachContext context) {
            calls.add("detach");
        }
    }

    private static final class FailingAttachBehavior extends AbstractFeatureBehavior {
        private final RuntimeException failure;
        private boolean failed;

        FailingAttachBehavior(RuntimeException failure) {
            this.failure = failure;
        }

        @Override
        protected void onInitialize(FeatureBehaviorAttachContext context) {
            if (!failed) {
                failed = true;
                throw failure;
            }
        }
    }

    private static final class SelfRemovingBehavior extends AbstractFeatureBehavior {
        private final List<String> calls;

        SelfRemovingBehavior(List<String> calls) {
            this.calls = calls;
        }

        @Override
        protected void onEnable(FeatureBehaviorEnableContext context) {
            calls.add("self");
            context.getFeature().removeBehavior(this);
        }
    }

    private static final class UniqueModule extends AbstractFeatureModule {}

    private static final class RecordingBehavior extends AbstractFeatureBehavior {
        private final String name;
        private final List<String> calls;

        RecordingBehavior(String name, List<String> calls) {
            this.name = name;
            this.calls = calls;
        }

        @Override
        protected void onInitialize(FeatureBehaviorAttachContext context) {
            calls.add("attach:" + name);
        }

        @Override
        protected void onEnable(FeatureBehaviorEnableContext context) {
            calls.add("enable:" + name);
        }

        @Override
        protected void onDisable(FeatureBehaviorDisableContext context) {
            calls.add("disable:" + name);
        }

        @Override
        protected void onUninitialize(FeatureBehaviorDetachContext context) {
            calls.add("detach:" + name);
        }
    }
}
