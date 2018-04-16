package ch.uzh.supersede.feedbacklibrary.utils;

public class StringUtility {
    public static boolean hasText(String s) {
        return s != null && s.length() > 1;
    }
}
