package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.config.ConfigService;
import me.bobulo.mine.pdam.config.InMemoryConfig;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.context.FeatureModuleAttachContext;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FeatureLifecycleTest {

    private MockedStatic<ThreadUtils> threads;
    private MockedStatic<EventUtils> events;
    private final List<String> calls = new ArrayList<>();
    private final TestFeature feature = TestFeature.of("lifecycle_test");
    private final RecordingModule module = new RecordingModule(calls);

    @BeforeEach
    public void simulateTheGameThreadAndEventBus() {
        threads = mockStatic(ThreadUtils.class);
        threads.when(ThreadUtils::isMainThread).thenReturn(true);
        events = mockStatic(EventUtils.class);
        feature.addModule(module);
        calls.clear();
    }

    @AfterEach
    public void releaseGameSimulation() {
        events.close();
        threads.close();
    }

    @Test
    public void ca03AttachmentProvidesTheLifetimeScopeBeforeRegistration() {
        FeatureService service = new FeatureService();
        service.registerFeature(feature);

        assertSame(feature, service.getFeature(feature.getId()));
        assertFalse(feature.isEnabled());
        assertNull(feature.getEnableScope());
        assertSame(feature.getLoadScope(), module.loadBinding.getContext().getScope());
        assertEquals(1, module.loadBinding.getInitializations());
        assertTrue(calls.isEmpty());
    }

    @Test
    public void ca03EnableUsesTheAlreadyAttachedResources() {
        feature.enable();

        assertTrue(feature.isEnabled());
        assertSame(feature.getLoadScope(), module.loadBinding.getContext().getScope());
        assertSame(feature.getEnableScope(), module.enableBinding.getContext().getScope());
        assertEquals(1, module.loadBinding.getInitializations());
        assertEquals(Arrays.asList("enable", "init:enable"), calls);
    }

    @Test
    public void ca03AutomaticEnableWaitsForTheModuleToFinishAttaching() {
        TestFeature automatic = TestFeature.initialized("automatic",
                scope -> scope.registerModule(new AutomaticEnableModule(calls)), scope -> {});

        assertTrue(automatic.isEnabled());
        assertEquals(Arrays.asList("attach", "init:load", "enable", "init:enable"), calls);
    }

    @Test
    public void ca03ConfiguredAutomaticEnablePreservesTheExistingModule() {
        ConfigService configuration = mock(ConfigService.class);
        when(configuration.getMainConfig()).thenReturn(new InMemoryConfig());

        try (MockedStatic<PDAM> application = mockStatic(PDAM.class)) {
            application.when(PDAM::getConfigService).thenReturn(configuration);
            TestFeature configured = TestFeature.initialized("configured", scope -> {
                scope.registerBehavior(new EnabledFeatureBehavior(true));
                scope.registerModule(new RecordingModule(calls));
            }, scope -> {});

            assertTrue(configured.isEnabled());
            assertEquals(Arrays.asList("attach", "init:load", "enable", "init:enable"), calls);
        }
    }

    @Test
    public void ca07ConfigurationCallbacksDoNotRepeatLifecycleTransitions() {
        ConfigService configuration = mock(ConfigService.class);
        when(configuration.getMainConfig()).thenReturn(new InMemoryConfig());

        try (MockedStatic<PDAM> application = mockStatic(PDAM.class)) {
            application.when(PDAM::getConfigService).thenReturn(configuration);
            EnabledFeatureBehavior enabled = new EnabledFeatureBehavior(false);
            feature.addBehavior(enabled);

            feature.enable();
            feature.disable();

            assertFalse(enabled.getEnabledConfig().get());
            assertEquals(Arrays.asList("enable", "init:enable", "disable", "close:enable"), calls);
        }
    }

    @Test
    public void ca04DisableClosesOnlyTheEnableScopeAfterTheCallback() {
        feature.enable();
        Scope loadScope = feature.getLoadScope();
        Scope enableScope = feature.getEnableScope();
        calls.clear();

        feature.disable();

        assertFalse(feature.isEnabled());
        assertSame(loadScope, feature.getLoadScope());
        assertNull(feature.getEnableScope());
        assertEquals(0, module.loadBinding.getClosures());
        assertEquals(1, module.enableBinding.getClosures());
        assertTrue(enableScope.getBindings().isEmpty());
        assertEquals(Arrays.asList("disable", "close:enable"), calls);
    }

    @Test
    public void ca04EnablingAgainCreatesAnotherEnableScopeAndKeepsTheLoadScope() {
        feature.enable();
        Scope loadScope = feature.getLoadScope();
        Scope enableScope = feature.getEnableScope();
        RecordingBinding previous = module.enableBinding;
        feature.disable();

        feature.enable();

        assertSame(loadScope, feature.getLoadScope());
        assertNotSame(enableScope, feature.getEnableScope());
        assertEquals(1, previous.getClosures());
        assertEquals(0, module.enableBinding.getClosures());
        assertEquals(1, module.loadBinding.getInitializations());
    }

    @Test
    public void ca05UnregisteringDisablesThenDetachesBeforeCleaningTheLoadScope() {
        FeatureService service = new FeatureService();
        service.registerFeature(feature);
        feature.enable();
        Scope loadScope = feature.getLoadScope();
        calls.clear();

        service.unregisterFeature(feature.getId());

        assertNull(service.getFeature(feature.getId()));
        assertFalse(feature.isEnabled());
        assertNull(feature.getEnableScope());
        assertNull(module.getFeature());
        assertTrue(feature.getModules().isEmpty());
        assertTrue(loadScope.getBindings().isEmpty());
        assertEquals(Arrays.asList("disable", "close:enable", "detach", "close:load"), calls);
    }

    @Test
    public void ca05UnregisteringADisabledFeatureDetachesWithoutEnablingIt() {
        FeatureService service = new FeatureService();
        service.registerFeature(feature);

        service.unregisterFeature(feature.getId());

        assertFalse(feature.isEnabled());
        assertEquals(1, module.loadBinding.getClosures());
        assertEquals(Arrays.asList("detach", "close:load"), calls);
    }

    @Test
    public void ca05UnregisterAllDetachesEveryRegisteredFeature() {
        FeatureService service = new FeatureService();
        TestFeature other = TestFeature.of("other");
        service.registerFeature(feature);
        service.registerFeature(other);

        service.unregisterAll();

        assertTrue(feature.getModules().isEmpty());
        assertTrue(feature.getLoadScope().getBindings().isEmpty());
        assertFalse(service.isFeatureRegistered(feature.getId()));
        assertFalse(service.isFeatureRegistered(other.getId()));
        assertEquals(1, module.loadBinding.getClosures());
    }

    @Test
    public void ca06AddedModuleAttachesThenEnablesAndRemovalDisablesThenDetaches() {
        TestFeature active = TestFeature.of("active");
        active.enable();

        active.addModule(new RecordingModule(calls));

        assertEquals(Arrays.asList("attach", "init:load", "enable", "init:enable"), calls);
        RecordingModule added = active.getModule(RecordingModule.class);
        active.removeModule(added);
        assertEquals(Arrays.asList("attach", "init:load", "enable", "init:enable",
                "disable", "detach"), calls);
        assertNull(added.getFeature());
    }

    @Test
    public void ca06DisabledFeatureOnlyAttachesAndDetachesTheModule() {
        TestFeature disabled = TestFeature.of("disabled");
        RecordingModule added = new RecordingModule(calls);

        disabled.addModule(added);
        disabled.removeModule(added);

        assertEquals(Arrays.asList("attach", "init:load", "detach"), calls);
        assertNull(added.getFeature());
    }

    @Test
    public void ca07RepeatedEnableAndDisableDoNotRepeatAttachmentOrCallbacks() {
        feature.enable();
        feature.enable();
        feature.disable();
        feature.disable();

        assertEquals(Arrays.asList("enable", "init:enable", "disable", "close:enable"), calls);
        assertEquals(1, module.loadBinding.getInitializations());
        assertSame(feature, module.getFeature());
    }

    @Test
    public void ca07RemovingAFeatureTwiceDoesNotDetachOrCloseAgain() {
        FeatureService service = new FeatureService();
        service.registerFeature(feature);

        service.unregisterFeature(feature.getId());
        service.unregisterFeature(feature.getId());

        assertEquals(Arrays.asList("detach", "close:load"), calls);
        assertEquals(1, module.loadBinding.getClosures());
    }

    private static class RecordingModule extends AbstractFeatureModule {
        private final List<String> calls;
        private RecordingBinding loadBinding;
        private RecordingBinding enableBinding;

        private RecordingModule(List<String> calls) {
            this.calls = calls;
        }

        @Override
        protected void onInitialize(FeatureModuleAttachContext context) {
            calls.add("attach");
            loadBinding = new RecordingBinding("load", calls);
            context.getScope().registerBinding(loadBinding);
        }

        @Override
        protected void onEnable(FeatureModuleEnableContext context) {
            calls.add("enable");
            enableBinding = new RecordingBinding("enable", calls);
            context.getScope().registerBinding(enableBinding);
        }

        @Override
        protected void onDisable(FeatureModuleDisableContext context) {
            assertSame(context.getScope(), enableBinding.getContext().getScope());
            assertEquals(0, enableBinding.getClosures());
            calls.add("disable");
        }

        @Override
        protected void onUninitialize(FeatureModuleDetachContext context) {
            assertSame(context.getScope(), loadBinding.getContext().getScope());
            assertEquals(0, loadBinding.getClosures());
            calls.add("detach");
        }
    }

    private static final class AutomaticEnableModule extends RecordingModule {
        private AutomaticEnableModule(List<String> calls) {
            super(calls);
        }

        @Override
        protected void onInitialize(FeatureModuleAttachContext context) {
            super.onInitialize(context);
            context.getFeature().enable();
        }
    }
}
