package com.objecteffects.temperature.math;

/*
 * https://stackoverflow.com/questions/5731863/mapping-a-numeric-range-onto-another
 */
public class MathUtils {
    public static double mapRange(final double sourceNumber, final double fromA,
            final double fromB, final double toA, final double toB) {
        final double deltaA = fromB - fromA;
        final double deltaB = toB - toA;
        final double scale = deltaB / deltaA;
        final double negA = -1 * fromA;
        final double offset = negA * scale + toA;
        final double finalNumber = sourceNumber * scale + offset;
        final int calcScale = (int) Math.pow(10, 0);

        return (int) Math.round(finalNumber * calcScale) / calcScale;
    }
}
