package me.frep.vulcan.spigot;

import java.util.Random;
import java.util.UUID;

/**
 * Minecraft {@code MathHelper} used by bundled NMS geometry.
 */
public final class MathHelper {
    public static final float SQRT_OF_TWO;
    private static final float[] SIN_TABLE = new float[65536];
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{
        0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8,
        31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
    };

    private MathHelper() {
    }

    public static float sin(float value) {
        return SIN_TABLE[(int) (value * 10430.378f) & 0xFFFF];
    }

    public static float cos(float value) {
        return SIN_TABLE[(int) (value * 10430.378f + 16384.0f) & 0xFFFF];
    }

    public static float sqrtFloat(float value) {
        return (float) Math.sqrt(value);
    }

    public static double sqrtDouble(double value) {
        return Math.sqrt(value);
    }

    public static int floorFloat(float value) {
        int truncated = (int) value;
        return value < (float) truncated ? truncated - 1 : truncated;
    }

    public static int floorDouble(double value) {
        int truncated = (int) value;
        return value < (double) truncated ? truncated - 1 : truncated;
    }

    public static long floorDoubleToLong(double value) {
        long truncated = (long) value;
        return value < (double) truncated ? truncated - 1L : truncated;
    }

    public static int ceilFloat(float value) {
        int truncated = (int) value;
        return value > (float) truncated ? truncated + 1 : truncated;
    }

    public static int wrapIndex(int index, int length) {
        return Math.floorMod(index, length);
    }

    public static float abs(float value) {
        return value >= 0.0f ? value : -value;
    }

    public static int abs(int value) {
        return value >= 0 ? value : -value;
    }

    public static float wrapDegrees(float value) {
        value %= 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    public static double wrapDegrees(double value) {
        value %= 360.0;
        if (value >= 180.0) {
            value -= 360.0;
        }
        if (value < -180.0) {
            value += 360.0;
        }
        return value;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }

    public static float lerp(float delta, float start, float end) {
        return start + delta * (end - start);
    }

    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }

    public static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    public static int smallestEncompassingPowerOfTwo(int value) {
        int bits = value - 1;
        bits |= bits >> 1;
        bits |= bits >> 2;
        bits |= bits >> 4;
        bits |= bits >> 8;
        bits |= bits >> 16;
        return bits + 1;
    }

    public static int log2DeBruijn(int value) {
        value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) value * 125613361L >> 27) & 0x1F];
    }

    public static int log2(int value) {
        return log2DeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    public static int ilog2(int value) {
        return log2(value);
    }

    public static int packRgb(int red, int green, int blue) {
        return (red << 16) + (green << 8) + blue;
    }

    public static long getBlockSeed(int x, int y, int z) {
        long seed = (long) (x * 3129871) ^ (long) z * 116129781L ^ (long) y;
        seed = seed * seed * 42317861L + seed * 11L;
        return seed >> 16;
    }

    public static int roundToInt(double value) {
        return floorDouble(value + 0.5);
    }

    public static UUID createInsecureUuid(Random random) {
        long most = random.nextLong() & 0xFFFFFFFFFFFF0FFFL | 0x4000L;
        long least = random.nextLong() & 0x3FFFFFFFFFFFFFFFL | Long.MIN_VALUE;
        return new UUID(most, least);
    }

    static {
        SQRT_OF_TWO = sqrtFloat(2.0f);
        for (int index = 0; index < SIN_TABLE.length; ++index) {
            SIN_TABLE[index] = (float) Math.sin((double) index * Math.PI * 2.0 / 65536.0);
        }
    }
}
