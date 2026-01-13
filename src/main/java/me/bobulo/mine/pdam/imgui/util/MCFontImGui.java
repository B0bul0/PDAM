package me.bobulo.mine.pdam.imgui.util;

import imgui.ImDrawList;
import imgui.ImGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

/**
 * Minecraft FontRenderer for ImGui
 */
@SideOnly(Side.CLIENT)
public final class MCFontImGui {

    private static final ResourceLocation ASCII_LOC = new ResourceLocation("textures/font/ascii.png");
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256]; // cache

    private static byte[] glyphWidthCache = null; // Cache
    private static final int[] COLOR_CODE = new int[32]; // Cache

    // From FontRenderer
    static {
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }

            if (Minecraft.getMinecraft().gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            COLOR_CODE[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public static void mcText(String text) {
        mcText(text, 0xFFFFFFFF, false, 1.0f);
    }

    public static void mcText(String text, float scale) {
        mcText(text, 0xFFFFFFFF, false, scale);
    }

    public static void mcText(String text, int color, boolean shadow, float scale) {
        if (text == null || text.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fr = mc.fontRendererObj;
        ImDrawList drawList = ImGui.getWindowDrawList();

        if (glyphWidthCache == null) {
            try {
                Field f = ReflectionHelper.findField(FontRenderer.class, "glyphWidth", "field_78287_e");
                glyphWidthCache = (byte[]) f.get(fr);
            } catch (Exception e) {
                glyphWidthCache = new byte[65536]; // Fallback
            }
        }

        float posX = ImGui.getCursorScreenPosX();
        float posY = ImGui.getCursorScreenPosY();

        // Ignore Random
        boolean boldStyle = false;
        boolean italicStyle = false;
        boolean underlineStyle = false;
        boolean strikethroughStyle = false;

        float alphaFloat = (color >> 24 & 255) / 255.0F;
        if (alphaFloat == 0.0F) alphaFloat = 1.0F;
        int alpha = (int) (alphaFloat * 255);

        int initR = (color >> 16) & 255;
        int initG = (color >> 8) & 255;
        int initB = (color & 255);

        int currentColor = colorToImGui(initR, initG, initB, alpha);

        int shadowR = (initR & 0xFC) >> 2;
        int shadowG = (initG & 0xFC) >> 2;
        int shadowB = (initB & 0xFC) >> 2;
        int shadowColor = colorToImGui(shadowR, shadowG, shadowB, alpha);

        ImGui.dummy(fr.getStringWidth(text) * scale, fr.FONT_HEIGHT * scale);

        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);

            if (c0 == '§' && i + 1 < text.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase().charAt(i + 1));

                if (i1 < 16) {
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;

                    if (i1 < 0) {
                        i1 = 15;
                    }

                    int mcRgb = COLOR_CODE[i1];

                    int r = (mcRgb >> 16) & 255;
                    int g = (mcRgb >> 8) & 255;
                    int b = (mcRgb & 255);

                    currentColor = colorToImGui(r, g, b, alpha);

                    shadowR = (r & 0xFC) >> 2;
                    shadowG = (g & 0xFC) >> 2;
                    shadowB = (b & 0xFC) >> 2;
                    shadowColor = colorToImGui(shadowR, shadowG, shadowB, alpha);

                } else if (i1 == 16) {
                    // ignore random style
                } else if (i1 == 17) {
                    boldStyle = true;
                } else if (i1 == 18) {
                    strikethroughStyle = true;
                } else if (i1 == 19) {
                    underlineStyle = true;
                } else if (i1 == 20) {
                    italicStyle = true;
                } else if (i1 == 21) {
                    // reset
                    boldStyle = false;
                    strikethroughStyle = false;
                    underlineStyle = false;
                    italicStyle = false;

                    currentColor = colorToImGui(initR, initG, initB, alpha);
                    shadowR = (initR & 0xFC) >> 2;
                    shadowG = (initG & 0xFC) >> 2;
                    shadowB = (initB & 0xFC) >> 2;
                    shadowColor = colorToImGui(shadowR, shadowG, shadowB, alpha);
                }

                ++i; // Skip next char
                continue;
            }

            int charIndex = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);

            boolean isUnicode = charIndex == -1 || mc.gameSettings.forceUnicodeFont;

            float f1 = (!isUnicode ? 1.0F : 0.5F) * scale;

            boolean unicodeShadowFlag = (c0 == 0 || isUnicode) && shadow;

            if (shadow) {
                float shadowX = posX + scale;
                float shadowY = posY + scale;

                if (unicodeShadowFlag) {
                    shadowX -= f1;
                    shadowY -= f1;
                }

                renderChar(drawList, fr, c0, charIndex, isUnicode, shadowX, shadowY, shadowColor, italicStyle, scale);

                if (boldStyle) {
                    shadowX += f1;
                    if (unicodeShadowFlag) {
                        shadowX -= f1;
                        shadowY -= f1;
                    }
                    renderChar(drawList, fr, c0, charIndex, isUnicode, shadowX, shadowY, shadowColor, italicStyle, scale);
                }
            }

            float charWidth = renderChar(drawList, fr, c0, charIndex, isUnicode, posX, posY, currentColor, italicStyle, scale);

            if (boldStyle) {
                renderChar(drawList, fr, c0, charIndex, isUnicode, posX + f1, posY, currentColor, italicStyle, scale);
                charWidth += f1;
            }

            if (strikethroughStyle) {
                float midY = posY + (fr.FONT_HEIGHT * scale / 2.0F);
                drawList.addRectFilled(posX, midY - scale, posX + charWidth, midY, currentColor);
            }

            if (underlineStyle) {
                float botY = posY + (fr.FONT_HEIGHT * scale);
                drawList.addRectFilled(posX + (-1 * scale), botY - scale, posX + charWidth, botY, currentColor);
            }

            posX += charWidth;
        }
    }

    private static float renderChar(ImDrawList drawList, FontRenderer fr, char ch, int index, boolean isUnicode, float x, float y, int color, boolean italic, float scale) {
        if (ch == ' ') {
            return 4.0F * scale;
        }

        if (isUnicode) {
            return renderUnicodeChar(drawList, ch, x, y, color, italic, scale);
        }

        return renderDefaultChar(drawList, fr, index, ch, x, y, color, italic, scale);
    }

    /**
     * From FontRenderer renderDefaultChar
     */
    private static float renderDefaultChar(ImDrawList drawList, FontRenderer fr, int chIndex, char c, float x, float y, int color, boolean italic, float scale) {
        int i = chIndex % 16 * 8;
        int j = chIndex / 16 * 8;
        int k = italic ? 1 : 0;

        int texId = getTextureId(Minecraft.getMinecraft(), ASCII_LOC);

        int l = fr.getCharWidth(c);
        float f = l - 0.01F;

        float u1 = i / 128.0F;
        float v1 = j / 128.0F;
        float x1 = x + k * scale;
        float y1 = y;

        float u2 = i / 128.0F;
        float v2 = (j + 7.99F) / 128.0F;
        float x2 = x - k * scale;
        float y2 = y + 7.99F * scale;

        float u3 = (i + f - 1.0F) / 128.0F;
        float v3 = j / 128.0F;
        float x3 = x + (f - 1.0F + k) * scale;
        float y3 = y;

        float u4 = (i + f - 1.0F) / 128.0F;
        float v4 = (j + 7.99F) / 128.0F;
        float x4 = x + (f - 1.0F - k) * scale;
        float y4 = y + 7.99F * scale;

        drawList.addImageQuad(texId,
          x1, y1,
          x3, y3,
          x4, y4,
          x2, y2,
          u1, v1,
          u3, v3,
          u4, v4,
          u2, v2,
          color
        );

        return l * scale;
    }

    /**
     * From FontRenderer renderUnicodeChar
     */
    private static float renderUnicodeChar(ImDrawList drawList, char ch, float x, float y, int color, boolean italic, float scale) {
        if (glyphWidthCache[ch] == 0) {
            return 0.0F;
        }

        int page = ch / 256;

        if (unicodePageLocations[page] == null) {
            unicodePageLocations[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", page));
        }

        ResourceLocation loc = unicodePageLocations[page];
        int texId = getTextureId(Minecraft.getMinecraft(), loc);

        int j = glyphWidthCache[ch] >>> 4;
        int k = glyphWidthCache[ch] & 15;
        float f = j;
        float f1 = k + 1;
        float f2 = (ch % 16 * 16) + f;
        float f3 = (ch & 255) / 16 * 16;
        float f4 = f1 - f - 0.02F;
        float f5 = italic ? 1.0F : 0.0F;

        float f5Scaled = f5 * scale;

        float u1 = f2 / 256.0F;
        float v1 = f3 / 256.0F;
        float x1 = x + f5Scaled;
        float y1 = y;

        float u2 = f2 / 256.0F;
        float v2 = (f3 + 15.98F) / 256.0F;
        float x2 = x - f5Scaled;
        float y2 = y + 7.99F * scale;

        float u3 = (f2 + f4) / 256.0F;
        float v3 = f3 / 256.0F;
        float x3 = x + (f4 / 2.0F * scale) + f5Scaled;
        float y3 = y;

        float u4 = (f2 + f4) / 256.0F;
        float v4 = (f3 + 15.98F) / 256.0F;
        float x4 = x + (f4 / 2.0F * scale) - f5Scaled;
        float y4 = y + 7.99F * scale;

        drawList.addImageQuad(texId,
          x1, y1,
          x3, y3,
          x4, y4,
          x2, y2,
          u1, v1,
          u3, v3,
          u4, v4,
          u2, v2,
          color
        );

        return ((f1 - f) / 2.0F + 1.0F) * scale;
    }

    private static int colorToImGui(int r, int g, int b, int a) {
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    private static int getTextureId(Minecraft mc, ResourceLocation loc) {
        ITextureObject tex = mc.getTextureManager().getTexture(loc);

        if (tex == null) {
            tex = new SimpleTexture(loc);
            mc.getTextureManager().loadTexture(loc, tex);
        }

        return tex.getGlTextureId();
    }

}