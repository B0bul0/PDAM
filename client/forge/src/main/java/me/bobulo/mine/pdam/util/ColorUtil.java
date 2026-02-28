package me.bobulo.mine.pdam.util;

/**
 * Utility class for color conversions.
 */
public final class ColorUtil {

    public static int toArgbInt(float[] rgba) {
        int r = clamp(Math.round(rgba[0] * 255));
        int g = clamp(Math.round(rgba[1] * 255));
        int b = clamp(Math.round(rgba[2] * 255));
        int a = rgba.length > 3 ? clamp(Math.round(rgba[3] * 255)) : 255;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static float[] toRgba(int color) {
        return new float[] {
          ((color >> 16) & 0xFF) / 255.0f, // R
          ((color >> 8) & 0xFF) / 255.0f,  // G
          (color & 0xFF) / 255.0f,         // B
          ((color >> 24) & 0xFF) / 255.0f  // A
        };
    }

    public static int toRgbInt(float[] rgb) {
        int clampR = clamp(Math.round(rgb[0] * 255));
        int clampG = clamp(Math.round(rgb[1] * 255));
        int clampB = clamp(Math.round(rgb[2] * 255));
        return (clampR << 16) | (clampG << 8) | clampB;
    }

    public static float[] toRgb(int color) {
        float[] rgb = new float[3];
        rgb[0] = ((color >> 16) & 0xFF) / 255.0f;
        rgb[1] = ((color >> 8) & 0xFF) / 255.0f;
        rgb[2] = (color & 0xFF) / 255.0f;
        return rgb;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
