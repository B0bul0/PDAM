package me.bobulo.mine.pdam.feature.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.bobulo.mine.pdam.util.LocaleUtils;
import me.bobulo.mine.pdam.util.TextComponentBuilder;
import net.minecraft.util.EnumChatFormatting;

import java.util.Base64;
import java.util.Collection;

public final class SkinExtractionUtils {

    private SkinExtractionUtils() {
    }

    public static SkinExtracted extracted(GameProfile gameProfile) {
        Collection<Property> textures = gameProfile.getProperties().get("textures");
        if (textures.isEmpty()) {
            return null;
        }

        Property skinProperty = textures.iterator().next();
        String value = skinProperty.getValue();
        String signature = skinProperty.getSignature();

        String valueBase64 = value == null ? "" : Base64.getEncoder().encodeToString(value.getBytes());
        String signatureBase64 = signature == null ? "" : Base64.getEncoder().encodeToString(signature.getBytes());

        return new SkinExtracted(gameProfile.getName(), valueBase64, signatureBase64);
    }

    public static void sendSkinInfoMessage(GameProfile gameProfile) {
        SkinExtracted skinExtracted = extracted(gameProfile);
        sendSkinInfoMessage(skinExtracted);
    }

    public static void sendSkinInfoMessage(SkinExtracted skinExtracted) {
        if (skinExtracted == null || skinExtracted.isEmpty()) {
            LocaleUtils.sendMessage("pdam.skin_extraction.skin_not_found");
            return;
        }

        String valueBase64 = skinExtracted.getValueBase64();
        String signatureBase64 = skinExtracted.getSignatureBase64();

        TextComponentBuilder.createTranslated("pdam.skin_extraction.message.title",
            skinExtracted.getName()
          )

          .withColor(EnumChatFormatting.YELLOW)
          .appendNewLine()
          .appendTranslated("pdam.skin_extraction.message.value_data")
          .withHoverTranslated("pdam.general.copy_to_clipboard")
          .withClickCopyToClipboard(valueBase64)
          .withColor(EnumChatFormatting.BLUE)

          .append(" | ")
          .withColor(EnumChatFormatting.WHITE)

          .appendTranslated("pdam.skin_extraction.message.signature_data")
          .withHoverTranslated("pdam.general.copy_to_clipboard")
          .withClickCopyToClipboard(signatureBase64)
          .withColor(EnumChatFormatting.BLUE)

          .sendToClient();
    }

}
