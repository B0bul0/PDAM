package me.bobulo.mine.pdam.mixin;

import me.bobulo.mine.pdam.PDAM;
import me.bobulo.mine.pdam.feature.sign.window.SignEditorWindow;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "openEditSign", at = @At("HEAD"), cancellable = true)
    public void onOpenEditSign(TileEntitySign signTile, CallbackInfo cir) {
        EntityPlayerSP self = (EntityPlayerSP) (Object) this;

        if (PDAM.getFeatureService().isFeatureEnabled("sign_editor")) {
            cir.cancel();
            SignEditorWindow.openSignEditor(signTile);
        }

    }

}