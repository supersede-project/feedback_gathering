package ch.uzh.supersede.feedbacklibrary.utils;

public final class CompareUtility {
    private CompareUtility() {
    }

    public static boolean oneOf(Object o, Object... objects) {
        for (Object o_ : objects) {
            if (o != null && o.equals(o_)) {
                return true;
            }
        }
        return false;
    }

    public static boolean notNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }
}
