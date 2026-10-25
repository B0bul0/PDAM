package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.module.AbstractFeatureModule;
import me.bobulo.mine.pdam.feature.module.FeatureModule;
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
import static org.mockito.Mockito.mockStatic;

class ModuleLifecycleContextTest {
    private MockedStatic<ThreadUtils> threads;
    private MockedStatic<EventUtils> events;
    private final TestFeature feature = TestFeature.of("module_contexts");
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
    void ca13EachCallbackReceivesItsPhaseContextAndFeature() {
        ContextModule module = new ContextModule(calls);
        feature.addModule(module);
        feature.enable();
        Scope activation = feature.getEnableScope();

        feature.removeModule(module);

        assertSame(feature, module.attach.getFeature());
        assertSame(feature, module.enable.getFeature());
        assertSame(feature, module.disable.getFeature());
        assertSame(feature, module.detach.getFeature());
        assertSame(feature.getLoadScope(), module.attach.getScope());
        assertSame(feature.getLoadScope(), module.detach.getScope());
        assertSame(activation, module.enable.getScope());
        assertSame(activation, module.disable.getScope());
        assertEquals(Arrays.asList("attach", "enable", "disable", "detach"), calls);
    }

    @Test
    void ca14EnableScopeModuleKeepsItsOwnerThroughDetach() {
        feature.enable();
        Scope activation = feature.getEnableScope();
        ContextModule module = new ContextModule(calls);
        activation.registerModule(module);

        feature.disable();

        assertSame(activation, module.attach.getScope());
        assertSame(activation, module.enable.getScope());
        assertSame(activation, module.disable.getScope());
        assertSame(activation, module.detach.getScope());
        assertNull(feature.getEnableScope());
        assertNull(feature.getModule(ContextModule.class));
        assertEquals(Arrays.asList("attach", "enable", "disable", "detach"), calls);
    }

    @Test
    void ca14ExplicitScopeActivationUsesThatScopeWithoutAFeatureActivation() {
        Scope owner = new Scope(feature);
        ContextModule module = new ContextModule(calls);
        owner.registerModule(module);

        owner.activate();
        owner.cleanup();

        assertSame(owner, module.attach.getScope());
        assertSame(owner, module.enable.getScope());
        assertSame(owner, module.disable.getScope());
        assertSame(owner, module.detach.getScope());
        assertNull(feature.getEnableScope());
        assertEquals(Arrays.asList("attach", "enable", "disable", "detach"), calls);
    }

    @Test
    void ca14ReenableRenewsTheActivationContextsWithoutRepeatingAttach() {
        ContextModule module = new ContextModule(calls);
        feature.addModule(module);
        FeatureModuleAttachContext attachment = module.attach;
        feature.enable();
        FeatureModuleEnableContext first = module.enable;
        feature.disable();

        feature.enable();

        assertSame(attachment, module.attach);
        assertSame(feature.getLoadScope(), attachment.getScope());
        assertSame(feature.getEnableScope(), module.enable.getScope());
        assertNotSame(first.getScope(), module.enable.getScope());
        assertSame(first.getScope(), module.disable.getScope());
        assertEquals(Arrays.asList("attach", "enable", "disable", "enable"), calls);
    }

    @Test
    void ca15AbstractHooksRegisterResourcesInThePhaseScope() {
        ResourceModule module = new ResourceModule(calls);
        feature.addModule(module);
        feature.enable();
        Scope activation = feature.getEnableScope();

        feature.disable();

        assertSame(feature.getLoadScope(), module.loadBinding.getContext().getScope());
        assertSame(activation, module.enableBinding.getContext().getScope());
        assertSame(activation, module.disable.getScope());
        assertEquals(0, module.loadBinding.getClosures());
        assertEquals(1, module.enableBinding.getClosures());
        feature.getLoadScope().cleanup();
        assertEquals(1, module.loadBinding.getClosures());
        assertNull(module.getFeature());
    }

    @Test
    void ca15FailedEnableStillReceivesTheActivationContextDuringRollback() {
        RuntimeException failure = new IllegalStateException("enable failed");
        ResourceModule module = new ResourceModule(calls);
        module.enableFailure = failure;
        feature.addModule(module);

        assertSame(failure, assertThrows(IllegalStateException.class, feature::enable));

        assertSame(module.enableBinding.getContext().getScope(), module.disable.getScope());
        assertEquals(1, module.enableBinding.getClosures());
        assertEquals(0, module.loadBinding.getClosures());
        assertFalse(module.isEnabled());
        assertNull(feature.getEnableScope());
        assertSame(feature, module.getFeature());
    }

    @Test
    void contextsRequireAFeatureScope() {
        assertThrows(NullPointerException.class, () -> new FeatureModuleAttachContext(null));
        assertThrows(NullPointerException.class, () -> new FeatureModuleEnableContext(null));
        assertThrows(NullPointerException.class, () -> new FeatureModuleDisableContext(null));
        assertThrows(NullPointerException.class, () -> new FeatureModuleDetachContext(null));
        assertThrows(IllegalStateException.class, () -> new FeatureModuleAttachContext(new Scope()));
        assertThrows(IllegalStateException.class, () -> new FeatureModuleEnableContext(new Scope()));
        assertThrows(IllegalStateException.class, () -> new FeatureModuleDisableContext(new Scope()));
        assertThrows(IllegalStateException.class, () -> new FeatureModuleDetachContext(new Scope()));
    }

    private static final class ContextModule implements FeatureModule {
        private final List<String> calls;
        private FeatureModuleAttachContext attach;
        private FeatureModuleEnableContext enable;
        private FeatureModuleDisableContext disable;
        private FeatureModuleDetachContext detach;

        private ContextModule(List<String> calls) {
            this.calls = calls;
        }

        @Override
        public void onAttach(FeatureModuleAttachContext context) {
            attach = context;
            calls.add("attach");
        }

        @Override
        public void enable(FeatureModuleEnableContext context) {
            enable = context;
            calls.add("enable");
        }

        @Override
        public void disable(FeatureModuleDisableContext context) {
            disable = context;
            calls.add("disable");
        }

        @Override
        public void onDetach(FeatureModuleDetachContext context) {
            detach = context;
            calls.add("detach");
        }
    }

    private static final class ResourceModule extends AbstractFeatureModule {
        private final List<String> calls;
        private RecordingBinding loadBinding;
        private RecordingBinding enableBinding;
        private FeatureModuleDisableContext disable;
        private RuntimeException enableFailure;

        private ResourceModule(List<String> calls) {
            this.calls = calls;
        }

        @Override
        protected void onInitialize(FeatureModuleAttachContext context) {
            assertSame(getFeature(), context.getFeature());
            loadBinding = new RecordingBinding("load", calls);
            context.getScope().registerBinding(loadBinding);
        }

        @Override
        protected void onEnable(FeatureModuleEnableContext context) {
            assertSame(getFeature(), context.getFeature());
            enableBinding = new RecordingBinding("enable", calls);
            context.getScope().registerBinding(enableBinding);
            if (enableFailure != null) {
                throw enableFailure;
            }
        }

        @Override
        protected void onDisable(FeatureModuleDisableContext context) {
            assertSame(getFeature(), context.getFeature());
            assertEquals(0, enableBinding.getClosures());
            disable = context;
        }
    }
}
