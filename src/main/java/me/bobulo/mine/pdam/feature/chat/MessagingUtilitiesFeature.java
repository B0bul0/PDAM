package me.bobulo.mine.pdam.feature.chat;

import me.bobulo.mine.pdam.binding.ForgeListenerBinding;
import me.bobulo.mine.pdam.binding.ImGuiListenerBinding;
import me.bobulo.mine.pdam.feature.AbstractFeature;
import me.bobulo.mine.pdam.feature.behavior.EnabledFeatureBehavior;
import me.bobulo.mine.pdam.feature.chat.window.SendChatMessageWindow;
import me.bobulo.mine.pdam.feature.context.FeatureEnableContext;
import me.bobulo.mine.pdam.feature.context.FeatureInitializeContext;
import me.bobulo.mine.pdam.feature.imgui.FeatureToolbarMenuImGuiRender;
import org.jetbrains.annotations.NotNull;

/**
 * Groups chat copying and the message composition window.
 */
public final class MessagingUtilitiesFeature extends AbstractFeature {

    public static final String FEATURE_ID = "messaging_utilities";

    private ImGuiListenerBinding[] windows;
    private ForgeListenerBinding[] listeners;

    public MessagingUtilitiesFeature() {
        super(FEATURE_ID);
    }

    @Override
    protected void onInitialize(@NotNull FeatureInitializeContext context) {
        windows = ImGuiListenerBinding.of(
          new SendChatMessageWindow()
        );
        listeners = ForgeListenerBinding.of(new ChatCopyListener());
        context.getScope().registerBehavior(new EnabledFeatureBehavior(true));
        context.getScope().registerBehavior(new FeatureToolbarMenuImGuiRender());
    }

    @Override
    protected void onEnable(@NotNull FeatureEnableContext context) {
        context.getScope().registerBindings(windows);
        context.getScope().registerBindings(listeners);
    }
}
