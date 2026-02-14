package com.linearity.opentc4;

import com.linearity.colorannotation.annotation.ARGBColor;
import com.linearity.colorannotation.annotation.RGBColor;

public record Color(int r,int g,int b,int a) {

    // ===== int ARGB 构造 =====
    public Color(@RGBColor int color) {
        this((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 255);
    }

    // ===== RGB 构造 =====
    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    // ===== getters =====
    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    public int getAlpha() {
        return a;
    }

    // ===== 转回 int ARGB =====
    public @RGBColor int getRGB() {
        return (r << 16) | (g << 8) | b;
    }
    public @ARGBColor int getARGB() {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
