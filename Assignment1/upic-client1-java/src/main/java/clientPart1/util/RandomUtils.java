package clientPart1.util;

import java.util.Random;

import static java.lang.Math.abs;

public final class RandomUtils {
    /**
     * to create a random int value
     * @param upperBound  the upper bound (inclusive)
     * @return
     */
    public static int newPositiveInt(int upperBound) {
        if (upperBound <= 0) {
            throw new IllegalArgumentException("Upper bound must be > 0");
        }

        // we add 1 to it because the Random is actually uses an exclusive upper bound.
        int inclusiveUpperBound = upperBound == Integer.MAX_VALUE ? upperBound : upperBound + 1;
        int result = abs(new Random().nextInt(inclusiveUpperBound));
        return result == 0 ? 1 : result;
    }

    private RandomUtils() {
        throw new UnsupportedOperationException();
    }
}
