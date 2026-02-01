package me.bobulo.mine.pdam.mixin;

import me.bobulo.mine.pdam.feature.world.WorldTime;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class MixinWorldClient {

    @Inject(method = "setWorldTime", at = @At("HEAD"), cancellable = true)
    private void overrideUpdateTime(long time, CallbackInfo ci) {
        if (!WorldTime.isFeatureEnabled()) {
            return;
        }

        WorldClient self = (WorldClient) (Object) this;

        self.provider.setWorldTime(WorldTime.WORLD_TIME.get());

        ci.cancel();
    }
}