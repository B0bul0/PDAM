package me.bobulo.mine.pdam.feature.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.bobulo.mine.pdam.util.LocaleUtils;
import me.bobulo.mine.pdam.util.TextComponentBuilder;
import net.minecraft.util.EnumChatFormatting;

import java.util.Base64;
import java.util.Collection;

public final class SkinInfoUtils {

    private SkinInfoUtils() {
    }

    public static void sendSkinInfoMessage(GameProfile profile) {
        Collection<Property> textures = profile.getProperties().get("textures");

        if (textures.isEmpty()) {
            LocaleUtils.sendMessage("pdam.skin_info.skin_not_found");
            return;
        }

        Property skinProperty = textures.iterator().next();
        String value = skinProperty.getValue();
        String signature = skinProperty.getSignature();

        String valueBase64 = Base64.getEncoder().encodeToString(value.getBytes());
        String signatureBase64 = signature == null ? "null" : Base64.getEncoder().encodeToString(signature.getBytes());

        TextComponentBuilder.createTranslated("pdam.skin_info.message.title",
            profile.getName()
          )

          .withColor(EnumChatFormatting.YELLOW)
          .appendNewLine()
          .appendTranslated("pdam.skin_info.message.value_data")
          .withHoverTranslated("pdam.general.copy_to_clipboard")
          .withClickCopyToClipboard(valueBase64)
          .withColor(EnumChatFormatting.BLUE)

          .append(" | ")
          .withColor(EnumChatFormatting.WHITE)

          .appendTranslated("pdam.skin_info.message.signature_data")
          .withHoverTranslated("pdam.general.copy_to_clipboard")
          .withClickCopyToClipboard(signatureBase64)
          .withColor(EnumChatFormatting.BLUE)

          .sendToClient();

    }

}
