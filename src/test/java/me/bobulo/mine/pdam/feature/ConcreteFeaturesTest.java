package me.bobulo.mine.pdam.feature;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.binding.Binding;
import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.config.ConfigService;
import me.bobulo.mine.pdam.config.InMemoryConfig;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.bungeebypass.BungeeBypassFeature;
import me.bobulo.mine.pdam.feature.chat.MessagingUtilitiesFeature;
import me.bobulo.mine.pdam.feature.chat.window.SendChatMessageWindow;
import me.bobulo.mine.pdam.feature.chunk.ChunkViewerFeature;
import me.bobulo.mine.pdam.feature.creative.ExpandedCreativeInventoryFeature;
import me.bobulo.mine.pdam.feature.designtools.*;
import me.bobulo.mine.pdam.feature.designtools.charactermap.CharacterMapWindow;
import me.bobulo.mine.pdam.feature.designtools.hologram.HologramMockupWindow;
import me.bobulo.mine.pdam.feature.designtools.item.window.ItemBuilderWindow;
import me.bobulo.mine.pdam.feature.entity.EntityInfoOverlayFeature;
import me.bobulo.mine.pdam.feature.entity.ShowInvisibleEntitiesFeature;
import me.bobulo.mine.pdam.feature.hitbox.HitBoxesFeature;
import me.bobulo.mine.pdam.feature.inventoryslot.InventorySlotInspectorFeature;
import me.bobulo.mine.pdam.feature.itemname.ItemNameDebugFeature;
import me.bobulo.mine.pdam.feature.packet.PacketMonitorFeature;
import me.bobulo.mine.pdam.feature.packet.PacketRateTracker;
import me.bobulo.mine.pdam.feature.packet.window.PacketLogWindow;
import me.bobulo.mine.pdam.feature.player.CoordinatesWindow;
import me.bobulo.mine.pdam.feature.player.FlyBoosterFeature;
import me.bobulo.mine.pdam.feature.player.FlyBoosterWindow;
import me.bobulo.mine.pdam.feature.player.PlayerUtilitiesFeature;
import me.bobulo.mine.pdam.feature.scoreboard.ScoreboardInspectorFeature;
import me.bobulo.mine.pdam.feature.scoreboard.ScoreboardInspectorWindow;
import me.bobulo.mine.pdam.feature.server.ServerInfoFeature;
import me.bobulo.mine.pdam.feature.server.ServerInfoWindow;
import me.bobulo.mine.pdam.feature.sign.SignEditorFeature;
import me.bobulo.mine.pdam.feature.signfinder.SignFinderFeature;
import me.bobulo.mine.pdam.feature.signfinder.SignFinderWindow;
import me.bobulo.mine.pdam.feature.skin.SkinExtractionFeature;
import me.bobulo.mine.pdam.feature.sound.SoundDebugFeature;
import me.bobulo.mine.pdam.feature.sound.window.SoundDebugWindow;
import me.bobulo.mine.pdam.feature.tooltip.NBTTooltipFeature;
import me.bobulo.mine.pdam.feature.update.UpdateChecker;
import me.bobulo.mine.pdam.feature.update.UpdateFeature;
import me.bobulo.mine.pdam.feature.world.WorldTimeFeature;
import me.bobulo.mine.pdam.imgui.toolbar.ToolbarItemWindow;
import me.bobulo.mine.pdam.log.LogHistory;
import me.bobulo.mine.pdam.scope.Scope;
import me.bobulo.mine.pdam.util.EventUtils;
import me.bobulo.mine.pdam.util.ThreadUtils;
import net.minecraftforge.fml.common.Loader;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.InOrder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConcreteFeaturesTest {
    private static final FeatureCase[] FEATURES = {
            new FeatureCase(SoundDebugFeature.class, "sound_debug", true, 2),
            new FeatureCase(NBTTooltipFeature.class, "nbt_tooltip", true, 1),
            new FeatureCase(ShowInvisibleEntitiesFeature.class, "show_invisible_entities", true, 0),
            new FeatureCase(EntityInfoOverlayFeature.class, "entity_info_overlay", true, 1),
            new FeatureCase(SkinExtractionFeature.class, "skin_extraction", false, 3),
            new FeatureCase(PacketMonitorFeature.class, "packet_monitor", true, 2),
            new FeatureCase(ScoreboardInspectorFeature.class, "scoreboard_inspector", true, 1),
            new FeatureCase(ServerInfoFeature.class, "server_info", true, 1),
            new FeatureCase(FlyBoosterFeature.class, "fly_booster", false, 1),
            new FeatureCase(MessagingUtilitiesFeature.class, "messaging_utilities", true, 2),
            new FeatureCase(SignEditorFeature.class, "sign_editor", true, 1),
            new FeatureCase(DesignToolsFeature.class, "design_tools", true, 8),
            new FeatureCase(InventorySlotInspectorFeature.class, "inventory_slot_inspector", true, 0),
            new FeatureCase(ExpandedCreativeInventoryFeature.class, "expanded_creative_inventory", true, 0),
            new FeatureCase(PlayerUtilitiesFeature.class, "player_utilities", true, 1),
            new FeatureCase(ItemNameDebugFeature.class, "item_name_debug", true, 1),
            new FeatureCase(WorldTimeFeature.class, "world_time", false, 0),
            new FeatureCase(BungeeBypassFeature.class, "bungee_bypass", false, 0),
            new FeatureCase(SignFinderFeature.class, "sign_finder", true, 2),
            new FeatureCase(HitBoxesFeature.class, "hitboxes", false, 1),
            new FeatureCase(ChunkViewerFeature.class, "chunk_viewer", false, 1),
            new FeatureCase(UpdateFeature.class, "update", true, 1)
    };

    @TestFactory
    Collection<DynamicTest> everyFeatureIsFinalAndHasOnlyAPublicNoArgumentConstructor() {
        List<DynamicTest> tests = new ArrayList<>();
        for (FeatureCase featureCase : FEATURES) {
            tests.add(DynamicTest.dynamicTest(featureCase.id, () -> verifyConstruction(featureCase)));
        }
        return tests;
    }

    @TestFactory
    Collection<DynamicTest> concreteFeaturesRegisterReactivateAndReleaseTheirResources() {
        List<DynamicTest> tests = new ArrayList<>();
        for (FeatureCase featureCase : FEATURES) {
            // Creative tab changes require Minecraft's real block registries and are checked in-game.
            if (featureCase.type != ExpandedCreativeInventoryFeature.class) {
                tests.add(DynamicTest.dynamicTest(featureCase.id, () -> verifyLifecycle(featureCase)));
            }
        }
        return tests;
    }

    @Test
    void toolbarWindowsKeepTheirStateAcrossReactivation() {
        try (GameSimulation game = new GameSimulation()) {
            game.start();
            ScoreboardInspectorFeature feature = new ScoreboardInspectorFeature();
            game.service.registerFeature(feature);
            ToolbarItemWindow window = feature.getEnableScope().get(ToolbarItemWindow.class);

            feature.disable();
            feature.enable();

            assertSame(window, feature.getEnableScope().get(ToolbarItemWindow.class));
            game.service.unregisterAll();
        }
    }

    @Test
    void invisibleEntityFlagFollowsTheConcreteFeatureLifecycle() {
        try (GameSimulation game = new GameSimulation()) {
            game.start();
            ShowInvisibleEntitiesFeature feature = new ShowInvisibleEntitiesFeature();
            game.service.registerFeature(feature);
            assertTrue(ShowInvisibleEntitiesFeature.showInvisibleEntities);

            feature.disable();
            assertFalse(ShowInvisibleEntitiesFeature.showInvisibleEntities);
            feature.enable();
            assertTrue(ShowInvisibleEntitiesFeature.showInvisibleEntities);
            game.service.unregisterAll();
            assertFalse(ShowInvisibleEntitiesFeature.showInvisibleEntities);
        }
    }

    @Test
    void soundListenerIsRemovedBeforeItsHistoryIsCleared() {
        try (GameSimulation game = new GameSimulation()) {
            game.start();
            SoundDebugFeature feature = new SoundDebugFeature();
            game.service.registerFeature(feature);
            ForgeListenerBinding listener = game.listeners.constructed().get(0);
            LogHistory<?> history = (LogHistory<?>) game.histories.constructed().get(0);

            feature.disable();

            InOrder cleanup = inOrder(listener, history);
            cleanup.verify(listener).close();
            cleanup.verify(history).clear();
            assertNull(feature.getSoundHistory());
            game.service.unregisterAll();
        }
    }

    @Test
    void packetListenerIsRemovedBeforeItsHistoryIsCleared() {
        try (GameSimulation game = new GameSimulation()) {
            game.start();
            PacketMonitorFeature feature = new PacketMonitorFeature();
            game.service.registerFeature(feature);
            ForgeListenerBinding listener = game.listeners.constructed().get(0);
            LogHistory<?> history = (LogHistory<?>) game.histories.constructed().get(0);

            feature.disable();

            InOrder cleanup = inOrder(listener, history);
            cleanup.verify(listener).close();
            cleanup.verify(history).clear();
            game.service.unregisterAll();
        }
    }

    private static void verifyConstruction(FeatureCase featureCase) throws ReflectiveOperationException {
        try (GameSimulation game = new GameSimulation()) {
            game.start();
            assertTrue(Modifier.isFinal(featureCase.type.getModifiers()));
            assertEquals(1, featureCase.type.getDeclaredConstructors().length);
            AbstractFeature feature = featureCase.type.getConstructor().newInstance();
            assertEquals(featureCase.id, feature.getId());
            assertFalse(feature.isEnabled());
            assertNull(feature.getEnableScope());
            assertTrue(feature.getModules().isEmpty());
        }
    }

    private static void verifyLifecycle(FeatureCase featureCase) throws ReflectiveOperationException {
        try (GameSimulation game = new GameSimulation()) {
            game.start();
            AbstractFeature feature = featureCase.type.getConstructor().newInstance();
            game.service.registerFeature(feature);
            assertEquals(featureCase.defaultEnabled, feature.isEnabled());
            assertEquals(1, feature.getBehaviors(EnabledFeatureBehavior.class).size());
            assertTrue(feature.getModules().isEmpty());

            feature.enable();
            Scope first = feature.getEnableScope();
            List<Binding<?>> firstBindings = new ArrayList<>(first.getBindings());
            assertEquals(featureCase.bindingCount, firstBindings.size());
            feature.disable();
            assertTrue(first.getBindings().isEmpty());
            assertTrue(first.getBehaviorRegistry().getBehaviors().isEmpty());
            for (Binding<?> binding : firstBindings) {
                verify(binding).close();
            }

            feature.enable();
            Scope second = feature.getEnableScope();
            assertNotSame(first, second);
            assertEquals(featureCase.bindingCount, second.getBindings().size());
            game.service.unregisterAll();
            assertFalse(feature.isEnabled());
            assertNull(game.service.getFeature(featureCase.id));
            assertTrue(second.getBindings().isEmpty());
            assertTrue(feature.getLoadScope().getBehaviorRegistry().getBehaviors().isEmpty());
        }
    }

    private static final class FeatureCase {
        private final Class<? extends AbstractFeature> type;
        private final String id;
        private final boolean defaultEnabled;
        private final int bindingCount;

        private FeatureCase(Class<? extends AbstractFeature> type, String id, boolean defaultEnabled, int bindingCount) {
            this.type = type;
            this.id = id;
            this.defaultEnabled = defaultEnabled;
            this.bindingCount = bindingCount;
        }
    }

    /** Keeps game windows, rendering services and update network requests outside lifecycle tests. */
    private static final class GameSimulation implements AutoCloseable {
        private final FeatureService service = new FeatureService();
        private final List<AutoCloseable> resources = new ArrayList<>();
        private MockedConstruction<ForgeListenerBinding> listeners;
        private MockedConstruction<?> histories;

        private void start() {
            MockedStatic<ThreadUtils> threads = mockStatic(ThreadUtils.class);
            resources.add(threads);
            threads.when(ThreadUtils::isMainThread).thenReturn(true);
            resources.add(mockStatic(EventUtils.class));
            MockedStatic<PDAM> application = mockStatic(PDAM.class);
            resources.add(application);
            ConfigService configuration = mock(ConfigService.class);
            when(configuration.getMainConfig()).thenReturn(new InMemoryConfig());
            application.when(PDAM::getConfigService).thenReturn(configuration);

            MockedStatic<Loader> forge = mockStatic(Loader.class);
            resources.add(forge);
            Loader loader = mock(Loader.class);
            forge.when(Loader::instance).thenReturn(loader);
            when(loader.getIndexedModList()).thenReturn(Collections.emptyMap());

            listeners = mockConstruction(ForgeListenerBinding.class,
                    (binding, context) -> when(binding.get()).thenReturn(context.arguments().get(0)));
            resources.add(listeners);
            resources.add(mockConstruction(ImGuiListenerBinding.class,
                    (binding, context) -> when(binding.get()).thenReturn(context.arguments().get(0))));
            histories = mockConstruction(LogHistory.class);
            resources.add(histories);
            stubConstruction(UpdateChecker.class);
            stubConstruction(PacketRateTracker.class);
            stubConstruction(SoundDebugWindow.class);
            stubConstruction(PacketLogWindow.class);
            stubConstruction(ScoreboardInspectorWindow.class);
            stubConstruction(ServerInfoWindow.class);
            stubConstruction(FlyBoosterWindow.class);
            stubConstruction(SendChatMessageWindow.class);
            stubConstruction(CharacterMapWindow.class);
            stubConstruction(MessageFormatterWindow.class);
            stubConstruction(TitleVisualizerWindow.class);
            stubConstruction(ActionBarVisualizerWindow.class);
            stubConstruction(HologramMockupWindow.class);
            stubConstruction(PlaySoundWindow.class);
            stubConstruction(ItemBuilderWindow.class);
            stubConstruction(SpawnParticleWindow.class);
            stubConstruction(CoordinatesWindow.class);
            stubConstruction(SignFinderWindow.class);
        }

        private <T> void stubConstruction(Class<T> type) {
            MockedConstruction<T> construction = mockConstruction(type);
            resources.add(construction);
        }

        @Override
        public void close() {
            for (int index = resources.size() - 1; index >= 0; index--) {
                try {
                    resources.get(index).close();
                } catch (Exception failure) {
                    throw new IllegalStateException("Failed to close game simulation resource " + index, failure);
                }
            }
        }
    }
}
