package me.bobulo.mine.pdam.feature;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class FeatureBoundValuesTest {

    private MockedStatic<ThreadUtils> threads;
    private MockedStatic<EventUtils> events;
    private final TestFeature feature = TestFeature.of("bound_values");
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
    void returnsAssignableLoadValuesWhileDisabled() {
        feature.getLoadScope().registerBinding(new RecordingBinding("load", calls));

        assertEquals(Collections.singletonList("load"), feature.getBoundValues(CharSequence.class));
    }

    @Test
    void preservesLoadThenEnableRegistrationOrder() {
        feature.getLoadScope().registerBindings(
          new RecordingBinding("load-first", calls), new RecordingBinding("load-second", calls));
        feature.enable();
        feature.getEnableScope().registerBindings(
          new RecordingBinding("enable-first", calls), new RecordingBinding("enable-second", calls));

        assertEquals(Arrays.asList("load-first", "load-second", "enable-first", "enable-second"),
          feature.getBoundValues(String.class));
    }

    @Test
    void returnsEmptyWhenNoBoundValueMatches() {
        feature.getLoadScope().registerBinding(new RecordingBinding("load", calls));
        feature.enable();
        feature.getEnableScope().registerBinding(new RecordingBinding("enable", calls));

        assertTrue(feature.getBoundValues(Number.class).isEmpty());
    }

    @Test
    void excludesEnableValuesAfterDisable() {
        feature.getLoadScope().registerBinding(new RecordingBinding("load", calls));
        feature.enable();
        feature.getEnableScope().registerBinding(new RecordingBinding("enable", calls));

        feature.disable();

        assertEquals(Collections.singletonList("load"), feature.getBoundValues(String.class));
    }

    @Test
    void usesOnlyTheCurrentEnableScopeAfterReenable() {
        feature.getLoadScope().registerBinding(new RecordingBinding("load", calls));
        feature.enable();
        feature.getEnableScope().registerBinding(new RecordingBinding("previous", calls));
        feature.disable();

        feature.enable();
        feature.getEnableScope().registerBinding(new RecordingBinding("current", calls));

        assertEquals(Arrays.asList("load", "current"), feature.getBoundValues(String.class));
    }

    @Test
    void modifyingReturnedValuesDoesNotChangeRegistrations() {
        feature.getLoadScope().registerBinding(new RecordingBinding("load", calls));
        feature.enable();
        feature.getEnableScope().registerBinding(new RecordingBinding("enable", calls));
        List<String> values = feature.getBoundValues(String.class);

        values.clear();

        assertEquals(Arrays.asList("load", "enable"), feature.getBoundValues(String.class));
    }
}
