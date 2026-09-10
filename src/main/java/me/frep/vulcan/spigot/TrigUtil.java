package me.frep.vulcan.spigot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TrigUtil {
    public static final double TWO_POW_24 = Math.pow(2.0, 24.0);

    private TrigUtil() {
    }

    public static int compareFloat(float value) {
        return Float.compare(value, 0.0f);
    }

    public static int compareFloat(Float value) {
        return compareFloat(value.floatValue());
    }

    public static int isKeepAlivePacket(float delta) {
        return compareFloat(delta);
    }

    public static double hypot2(double x, double z) {
        return Math.sqrt(x * x + z * z);
    }

    public static double hypot3(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public static String formatDecimal(double value) {
        return new DecimalFormat("#.##").format(value);
    }

    public static float angleDistance(float first, float second) {
        float wrappedFirst = first % 360.0f;
        float wrappedSecond = second % 360.0f;
        float delta = Math.abs(wrappedFirst - wrappedSecond);
        return (float) Math.min(360.0 - (double) delta, (double) delta);
    }

    public static double mean(Collection<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        int count = 0;
        for (Number number : values) {
            sum += number.doubleValue();
            ++count;
        }
        return count == 0 ? 0.0 : sum / (double) count;
    }

    public static double variance(Collection<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        double average = mean(values);
        double sum = 0.0;
        for (Number number : values) {
            sum += Math.pow(number.doubleValue() - average, 2.0);
        }
        return sum;
    }

    public static double standardDeviation(Collection<? extends Number> values) {
        return Math.sqrt(variance(values));
    }

    public static double median(Collection<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        List<Double> sorted = new ArrayList<Double>();
        for (Number number : values) {
            sorted.add(number.doubleValue());
        }
        Collections.sort(sorted);
        int size = sorted.size();
        if (size % 2 != 0) {
            return sorted.get(size / 2);
        }
        return (sorted.get((size - 1) / 2) + sorted.get(size / 2)) / 2.0;
    }

    public static double skewness(Collection<? extends Number> values) {
        double average = mean(values);
        double medianValue = median(values);
        double deviation = standardDeviation(values);
        if (deviation == 0.0) {
            return 0.0;
        }
        return 3.0 * (average - medianValue) / deviation;
    }

    public static Number mode(Collection<? extends Number> values) {
        Map<Number, Integer> counts = new HashMap<Number, Integer>();
        for (Number number : values) {
            Integer current = counts.get(number);
            counts.put(number, current == null ? 1 : current + 1);
        }
        Number best = null;
        int bestCount = -1;
        for (Map.Entry<Number, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > bestCount) {
                best = entry.getKey();
                bestCount = entry.getValue();
            }
        }
        return best;
    }

    public static Double mode(double[] values) {
        Map<Double, Integer> counts = new HashMap<Double, Integer>();
        for (double value : values) {
            Integer current = counts.get(value);
            counts.put(value, current == null ? 1 : current + 1);
        }
        Double best = null;
        int bestCount = -1;
        for (Map.Entry<Double, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > bestCount) {
                best = entry.getKey();
                bestCount = entry.getValue();
            }
        }
        return best;
    }

    public static double round(double value, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("scale must be non-negative");
        }
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static int mostFrequentCount(Collection<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }
        Map<Number, Integer> counts = new HashMap<Number, Integer>();
        int best = 0;
        for (Number number : values) {
            Integer current = counts.get(number);
            int next = current == null ? 1 : current + 1;
            counts.put(number, next);
            if (next > best) {
                best = next;
            }
        }
        return best;
    }

    public static double wrapAngle(double value, double modulus) {
        if (modulus == 0.0) {
            return value;
        }
        if (Math.abs(modulus) < 0.001) {
            return value;
        }
        return value - Math.floor(value / modulus) * modulus;
    }

    public static double angleDiff(double first, double second) {
        double delta = Math.abs(first - second) % 360.0;
        return delta > 180.0 ? 360.0 - delta : delta;
    }

    public static double kurtosis(Collection<? extends Number> values) {
        if (values == null || values.size() < 4) {
            return 0.0;
        }
        double average = mean(values);
        double deviation = standardDeviation(values);
        if (deviation == 0.0) {
            return 0.0;
        }
        double fourthMoment = 0.0;
        int count = 0;
        for (Number number : values) {
            double normalized = (number.doubleValue() - average) / deviation;
            fourthMoment += normalized * normalized * normalized * normalized;
            ++count;
        }
        return fourthMoment / (double) count;
    }

    public static int distinctCount(Collection<?> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }
        return new java.util.HashSet<Object>(values).size();
    }

    public static double clicksPerSecond(Collection<? extends Number> intervals) {
        double average = mean(intervals);
        if (average == 0.0) {
            return 0.0;
        }
        return 20.0 / average * 50.0;
    }

    public static double gcd(double left, double right) {
        left = Math.abs(left);
        right = Math.abs(right);
        while (right > 0.001) {
            double remainder = left % right;
            left = right;
            right = remainder;
        }
        return left;
    }

    public static long gcd(long left, long right) {
        left = Math.abs(left);
        right = Math.abs(right);
        while (right != 0L) {
            long remainder = left % right;
            left = right;
            right = remainder;
        }
        return left;
    }
}
