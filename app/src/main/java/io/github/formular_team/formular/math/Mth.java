package io.github.formular_team.formular.math;

public final class Mth {
    private Mth() {}

    public static final float PI = (float) Math.PI;

    public static float sqrt(final float a) {
        return (float) Math.sqrt(a);
    }

    public static float sin(final float a) {
        return (float) Math.sin(a);
    }

    public static float cos(final float a) {
        return (float) Math.cos(a);
    }

    public static float tan(final float a) {
        return (float) Math.tan(a);
    }

    public static float ceil(final float a) {
        return (float) Math.ceil(a);
    }

    public static float floor(final float a) {
        return (float) Math.floor(a);
    }

    public static float round(final float a) {
        return (float) Math.round(a);
    }

    public static float acos(final float a) {
        return (float) Math.acos(a);
    }

    public static float atan2(final float a, final float b) {
        return (float) Math.atan2(a,b);
    }

    public static float clamp(float value, float min, float max)
    {
        return Math.max( min, Math.min( max, value ) );
    }
}
