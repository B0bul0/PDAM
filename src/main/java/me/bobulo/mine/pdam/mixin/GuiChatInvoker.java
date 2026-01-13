package me.bobulo.mine.pdam.mixin;

import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiChat.class)
public interface GuiChatInvoker {

    @Invoker("setText")
    void invokeSetText(String newChatText, boolean shouldOverwrite);

}
