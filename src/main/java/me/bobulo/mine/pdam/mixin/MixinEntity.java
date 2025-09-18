package me.bobulo.mine.pdam.mixin;

import me.bobulo.mine.pdam.feature.entity.ShowInvisibleEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "isInvisibleToPlayer", at = @At("HEAD"), cancellable = true)
    public void onIsInvisibleToPlayer(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;

        if (self.equals(Minecraft.getMinecraft().thePlayer)) {
            return;
        }

        if (ShowInvisibleEntities.showInvisibleEntities) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}