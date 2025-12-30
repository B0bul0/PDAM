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

        String url = value == null ? "" : extractSkinUrlFromValue(value);

        return new SkinExtracted(gameProfile.getName(), value, signature, url);
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
        String url = skinExtracted.getUrl();

        TextComponentBuilder.createTranslated("pdam.skin_extraction.message.title",
            skinExtracted.getName()
          )

          .withColor(EnumChatFormatting.YELLOW)
          .appendNewLine()
          .appendTranslated("pdam.skin_extraction.message.value_data")
          .withHoverTranslated("pdam.general.copy_to_clipboard")
          .withClickCopyToClipboard(valueBase64)
          .withColor(EnumChatFormatting.AQUA)

          .append(" | ")
          .withColor(EnumChatFormatting.WHITE)

          .appendTranslated("pdam.skin_extraction.message.signature_data")
          .withHoverTranslated("pdam.general.copy_to_clipboard")
          .withClickCopyToClipboard(signatureBase64)
          .withColor(EnumChatFormatting.AQUA)

          .append(" | ")
          .withColor(EnumChatFormatting.WHITE)

          .appendTranslated("pdam.skin_extraction.message.url")
          .withHoverTranslated("pdam.general.copy_to_clipboard")
          .withClickCopyToClipboard(url)
          .withColor(EnumChatFormatting.AQUA)

          .sendToClient();
    }

    public static String extractSkinUrlFromValue(String valueBase64) {
        byte[] decodedBytes = Base64.getDecoder().decode(valueBase64);
        String json = new String(decodedBytes);
        try {
            int urlStartIndex = json.indexOf("http");
            if (urlStartIndex == -1) {
                return "";
            }
            int urlEndIndex = json.indexOf("\"", urlStartIndex);
            if (urlEndIndex == -1) {
                return "";
            }
            return json.substring(urlStartIndex, urlEndIndex);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

}
