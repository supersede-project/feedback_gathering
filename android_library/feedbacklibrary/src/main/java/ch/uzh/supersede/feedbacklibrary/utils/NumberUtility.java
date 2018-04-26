package ch.uzh.supersede.feedbacklibrary.utils;

public class NumberUtility {

    private NumberUtility() {
    }

    public static int divide(int a, int b) {
        return divide(a, (float) b);
    }

    public static int divide(int a, float b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero.");
        }
        return (int) ((float) a / b);
    }

    public static int multiply(int a, float b) {
        return (int) (a * b);
    }

    public static int multiply(int a, double b) {
        return (int) (a * b);
    }

    public static int randomPosition(Object[] array) {
        if (array == null) {
            throw new ArrayIndexOutOfBoundsException("Array is null");
        }
        return randomInt(0, array.length - 1);
    }

    public static int randomInt(int min, int max) {
        if (max < min) {
            return 0;
        }
        int diff = (max - min) + 1;
        int random = multiply(diff, Math.random());
        if (diff == random) {
            return randomInt(min, max);
        }
        return min + random;
    }
}
