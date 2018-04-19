package ch.uzh.supersede.feedbacklibrary.utils;

public class StringUtility {

    private StringUtility() {
    }

    public static boolean hasText(String s) {
        return s != null && s.length() > 1;
    }

    public static boolean equals(String a, String b) {
        if (hasText(a) && hasText(b)) {
            return a.equals(b);
        }
        return false;
    }
}
