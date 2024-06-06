package net.astr0.astech.gui;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class TintColor {
    private int _color = 0;
    private int _alpha = 0;
    private int _green = 0;
    private int _blue = 0;
    private int _red = 0;

    private final Vector3f fogColor = new Vector3f();

    public TintColor(int r, int g, int b, int a) {
        validateColorComponent(r, "Red");
        validateColorComponent(g, "Green");
        validateColorComponent(b, "Blue");
        validateColorComponent(a, "Alpha");

        _alpha = a;
        _red = r;
        _green = g;
        _blue = b;
        _color = (a << 24) | (r << 16) | (g << 8) | b;
        fogColor.set(r / 255.0f, g / 255.0f, b / 255.0f);
    }

    public TintColor(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public TintColor(int ARGB) {
        this(
            ((ARGB >> 16) & 0xFF),
            ((ARGB >> 8) & 0xFF),
            ((ARGB) & 0xFF),
            ((ARGB >> 24) & 0xFF)
        );
    }

    public static TintColor fromHex(String hex, int alpha) {
        // Remove the hash at the beginning if it's there
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Check if the hex string is exactly 6 characters long
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Hex color string must be 6 characters long.");
        }

        // Check if the hex string contains only valid hexadecimal characters
        if (!hex.matches("[0-9A-Fa-f]{6}")) {
            throw new IllegalArgumentException("Hex color string contains invalid characters.");
        }

        // Parse the hex string to integer values
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        return new TintColor(r, g, b, alpha);
    }

    public static TintColor fromHex(String hex) {
        return  fromHex(hex, 255);
    }

    public Vector4f getAsNormalisedRenderColor() {
        return new Vector4f(_red / 255.0f, _green / 255.0f, _blue / 255.0f, _alpha / 255.0f);
    }

    public int getTintColor() {
        return _color;
    }

    public TintColor darkened(int amount) {
        return new TintColor(
                Math.max(0, _red - amount),
                Math.max(0, _green - amount),
                Math.max(0, _blue - amount),
                _alpha
        );
    }

    public Vector3f getFogColor() {
        return fogColor;
    }

    private void validateColorComponent(int value, String componentName) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException(componentName + " value must be between 0 and 255. Provided value: " + value);
        }
    }
}