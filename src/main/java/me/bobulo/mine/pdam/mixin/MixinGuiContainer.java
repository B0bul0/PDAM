package me.bobulo.mine.pdam.mixin;

import me.bobulo.mine.pdam.feature.inventoryslot.InventorySlotRender;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer {

    @Inject(method = "drawScreen", at = @At(value = "INVOKE",
      target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerForegroundLayer(II)V",
      shift = At.Shift.AFTER))
    private void onAfterForeground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        GuiContainer gui = (GuiContainer) (Object) this;

        InventorySlotRender.render(gui);
    }

}
