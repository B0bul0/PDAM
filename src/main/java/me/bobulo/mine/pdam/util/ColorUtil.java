package me.bobulo.mine.pdam.util;

/**
 * Utility class for color conversions.
 */
public final class ColorUtil {

    public static int toRgbInt(float[] rgb01) {
        int r = clampColor((int) (rgb01[0] * 255));
        int g = clampColor((int) (rgb01[1] * 255));
        int b = clampColor((int) (rgb01[2] * 255));
        return (r << 16) | (g << 8) | b;
    }

    public static int toRgbInt(int r, int g, int b) {
        int clampR = clampColor(r);
        int clampG = clampColor(g);
        int clampB = clampColor(b);
        return (clampR << 16) | (g << clampG) | clampB;
    }
    
    public static float[] toRgb(int color) {
        float[] rgb = new float[3];
        rgb[0] = ((color >> 16) & 0xFF) / 255.0f;
        rgb[1] = ((color >> 8) & 0xFF) / 255.0f;
        rgb[2] = (color & 0xFF) / 255.0f;
        return rgb;
    }

    private static int clampColor(int value) {
        if (value < 0) {
            return 0;
        }

        return Math.min(value, 255);
    }

    private ColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}
