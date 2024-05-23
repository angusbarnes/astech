package net.astr0.astech.Fluid.helpers;

import org.joml.Vector3f;

public class TintColor {
    private int _color = 0;
    private int _alpha = 0;
    private final Vector3f fogColor = new Vector3f();

    public TintColor(int r, int g, int b, int a) {
        validateColorComponent(r, "Red");
        validateColorComponent(g, "Green");
        validateColorComponent(b, "Blue");
        validateColorComponent(a, "Alpha");

        _alpha = a;
        _color = (a << 24) | (r << 16) | (g << 8) | b;
        fogColor.set(r / 255.0f, g / 255.0f, b / 255.0f);
    }

    public TintColor(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public int getTintColor() {
        return _color;
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