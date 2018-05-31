package ch.uzh.supersede.feedbacklibrary.utils;

public class ObjectUtility {

    private ObjectUtility() {
    }

    public static <T> T nvl(T value, T valueIfNull) {
        if (value == null) {
            return valueIfNull;
        }
        return value;
    }
}
