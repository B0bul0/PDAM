package me.bobulo.mine.pdam.mixin;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.imgui.ImGuiRenderer;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Unique
    private static final Logger log = LogManager.getLogger(MinecraftMixin.class);

    @Inject(method = "startGame", at = @At("TAIL"))
    private void onStartGame(CallbackInfo ci) {
        PDAM.getImGuiRenderer().init();
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/achievement/GuiAchievement;updateAchievementWindow()V"))
    private void onRunGameLoop(CallbackInfo ci) {
        Minecraft.getMinecraft().mcProfiler.startSection("ImGui Render");
        try {
            PDAM.getImGuiRenderer().renderFrame();
        } catch (Exception exception) {
            log.error("ImGui frame render error", exception);
        } finally {
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        PDAM.getImGuiRenderer().shutdown();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onRunTick(CallbackInfo ci) {
        PDAM.getImGuiRenderer().onTick();
    }

}
