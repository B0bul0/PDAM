package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.feature.behavior.AbstractFeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.CallbackFeatureBehavior;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorAttachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDetachContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorDisableContext;
import me.bobulo.mine.pdam.feature.behavior.context.FeatureBehaviorEnableContext;
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

class BehaviorLifecycleContextTest {
    private MockedStatic<ThreadUtils> threads;
    private MockedStatic<EventUtils> events;
    private final TestFeature feature = TestFeature.of("behavior_contexts");
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
    void ca17EachCallbackReceivesItsPhaseContextAndFeature() {
        ContextBehavior behavior = new ContextBehavior(calls);
        feature.addBehavior(behavior);
        feature.enable();
        Scope activation = feature.getEnableScope();

        feature.removeBehavior(behavior);

        assertSame(feature, behavior.attach.getFeature());
        assertSame(feature, behavior.enable.getFeature());
        assertSame(feature, behavior.disable.getFeature());
        assertSame(feature, behavior.detach.getFeature());
        assertSame(feature.getLoadScope(), behavior.attach.getScope());
        assertSame(feature.getLoadScope(), behavior.detach.getScope());
        assertSame(activation, behavior.enable.getScope());
        assertSame(activation, behavior.disable.getScope());
        assertEquals(Arrays.asList("attach", "enable", "disable", "detach"), calls);
    }

    @Test
    void ca17EnableScopeBehaviorKeepsItsOwnerThroughDetach() {
        feature.enable();
        Scope activation = feature.getEnableScope();
        ContextBehavior behavior = new ContextBehavior(calls);
        activation.registerBehavior(behavior);

        feature.disable();

        assertSame(activation, behavior.attach.getScope());
        assertSame(activation, behavior.enable.getScope());
        assertSame(activation, behavior.disable.getScope());
        assertSame(activation, behavior.detach.getScope());
        assertNull(feature.getEnableScope());
        assertFalse(feature.hasBehavior(ContextBehavior.class));
        assertEquals(Arrays.asList("attach", "enable", "disable", "detach"), calls);
    }

    @Test
    void ca17ExplicitScopeActivationUsesThatScopeWithoutAFeatureActivation() {
        Scope owner = new Scope(feature);
        ContextBehavior behavior = new ContextBehavior(calls);
        owner.registerBehavior(behavior);

        owner.activate();
        owner.cleanup();

        assertSame(owner, behavior.attach.getScope());
        assertSame(owner, behavior.enable.getScope());
        assertSame(owner, behavior.disable.getScope());
        assertSame(owner, behavior.detach.getScope());
        assertNull(feature.getEnableScope());
        assertEquals(Arrays.asList("attach", "enable", "disable", "detach"), calls);
    }

    @Test
    void ca17ReenableRenewsTheActivationContextsWithoutRepeatingAttach() {
        ContextBehavior behavior = new ContextBehavior(calls);
        feature.addBehavior(behavior);
        FeatureBehaviorAttachContext attachment = behavior.attach;
        feature.enable();
        FeatureBehaviorEnableContext first = behavior.enable;
        feature.disable();

        feature.enable();

        assertSame(attachment, behavior.attach);
        assertSame(feature.getLoadScope(), attachment.getScope());
        assertSame(feature.getEnableScope(), behavior.enable.getScope());
        assertNotSame(first.getScope(), behavior.enable.getScope());
        assertSame(first.getScope(), behavior.disable.getScope());
        assertEquals(Arrays.asList("attach", "enable", "disable", "enable"), calls);
    }

    @Test
    void ca17AbstractHooksRegisterResourcesInThePhaseScope() {
        ResourceBehavior behavior = new ResourceBehavior(calls);
        feature.addBehavior(behavior);
        feature.enable();
        Scope activation = feature.getEnableScope();

        feature.disable();

        assertSame(feature.getLoadScope(), behavior.loadBinding.getContext().getScope());
        assertSame(activation, behavior.enableBinding.getContext().getScope());
        assertSame(activation, behavior.disable.getScope());
        assertEquals(0, behavior.loadBinding.getClosures());
        assertEquals(1, behavior.enableBinding.getClosures());
        feature.getLoadScope().cleanup();
        assertSame(feature.getLoadScope(), behavior.detach.getScope());
        assertSame(feature, behavior.detach.getFeature());
        assertEquals(1, behavior.loadBinding.getClosures());
        assertNull(behavior.getFeature());
    }

    @Test
    void ca17FailedEnableStillReceivesTheActivationContextDuringRollback() {
        RuntimeException failure = new IllegalStateException("enable failed");
        ResourceBehavior behavior = new ResourceBehavior(calls);
        behavior.enableFailure = failure;
        feature.addBehavior(behavior);

        assertSame(failure, assertThrows(IllegalStateException.class, feature::enable));

        assertSame(behavior.enableBinding.getContext().getScope(), behavior.disable.getScope());
        assertEquals(1, behavior.enableBinding.getClosures());
        assertEquals(0, behavior.loadBinding.getClosures());
        assertFalse(behavior.isEnabled());
        assertNull(feature.getEnableScope());
        assertSame(feature, behavior.getFeature());
    }

    @Test
    void ca17CallbackConsumersReceiveTheirPhaseContexts() {
        CallbackContexts callbacks = new CallbackContexts();
        feature.addBehavior(CallbackFeatureBehavior.of(callbacks::onEnable, callbacks::onDisable));
        feature.enable();
        Scope activation = feature.getEnableScope();

        feature.disable();

        assertSame(feature, callbacks.enable.getFeature());
        assertSame(feature, callbacks.disable.getFeature());
        assertSame(activation, callbacks.enable.getScope());
        assertSame(activation, callbacks.disable.getScope());
    }

    @Test
    void ca17SinglePhaseCallbackConsumersReceiveTheirContexts() {
        CallbackContexts callbacks = new CallbackContexts();
        feature.addBehavior(CallbackFeatureBehavior.enableOnly(callbacks::onEnable));
        feature.addBehavior(CallbackFeatureBehavior.disableOnly(callbacks::onDisable));
        feature.enable();
        Scope activation = feature.getEnableScope();

        feature.disable();

        assertSame(activation, callbacks.enable.getScope());
        assertSame(activation, callbacks.disable.getScope());
        assertSame(feature, callbacks.enable.getFeature());
        assertSame(feature, callbacks.disable.getFeature());
    }

    private static final class ContextBehavior implements FeatureBehavior {
        private final List<String> calls;
        private FeatureBehaviorAttachContext attach;
        private FeatureBehaviorEnableContext enable;
        private FeatureBehaviorDisableContext disable;
        private FeatureBehaviorDetachContext detach;

        private ContextBehavior(List<String> calls) {
            this.calls = calls;
        }

        @Override
        public void onAttach(FeatureBehaviorAttachContext context) {
            attach = context;
            calls.add("attach");
        }

        @Override
        public void enable(FeatureBehaviorEnableContext context) {
            enable = context;
            calls.add("enable");
        }

        @Override
        public void disable(FeatureBehaviorDisableContext context) {
            disable = context;
            calls.add("disable");
        }

        @Override
        public void onDetach(FeatureBehaviorDetachContext context) {
            detach = context;
            calls.add("detach");
        }
    }

    private static final class ResourceBehavior extends AbstractFeatureBehavior {
        private final List<String> calls;
        private RecordingBinding loadBinding;
        private RecordingBinding enableBinding;
        private FeatureBehaviorDisableContext disable;
        private FeatureBehaviorDetachContext detach;
        private RuntimeException enableFailure;

        private ResourceBehavior(List<String> calls) {
            this.calls = calls;
        }

        @Override
        protected void onInitialize(FeatureBehaviorAttachContext context) {
            assertSame(getFeature(), context.getFeature());
            loadBinding = new RecordingBinding("load", calls);
            context.getScope().registerBinding(loadBinding);
        }

        @Override
        protected void onEnable(FeatureBehaviorEnableContext context) {
            assertSame(getFeature(), context.getFeature());
            enableBinding = new RecordingBinding("enable", calls);
            context.getScope().registerBinding(enableBinding);
            if (enableFailure != null) {
                throw enableFailure;
            }
        }

        @Override
        protected void onDisable(FeatureBehaviorDisableContext context) {
            assertSame(getFeature(), context.getFeature());
            assertEquals(0, enableBinding.getClosures());
            disable = context;
        }

        @Override
        protected void onUninitialize(FeatureBehaviorDetachContext context) {
            assertSame(getFeature(), context.getFeature());
            assertEquals(0, loadBinding.getClosures());
            detach = context;
        }
    }

    private static final class CallbackContexts {
        private FeatureBehaviorEnableContext enable;
        private FeatureBehaviorDisableContext disable;

        private void onEnable(FeatureBehaviorEnableContext context) {
            enable = context;
        }

        private void onDisable(FeatureBehaviorDisableContext context) {
            disable = context;
        }
    }
}
