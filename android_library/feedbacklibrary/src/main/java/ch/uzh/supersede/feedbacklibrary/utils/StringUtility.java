package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.List;

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

    public static String concatWithDelimiter(String delimiter, List<String> tokens) {
        return concatWithDelimiter(delimiter, tokens.toArray(new String[tokens.size()]));
    }

    public static String concatWithDelimiter(String delimiter, String... tokens) {
        String finalString = "";
        if (tokens == null || tokens.length == 0){
            return finalString;
        }
        for (String t : tokens) {
            finalString = finalString.concat(t).concat(delimiter);
        }
        return finalString.substring(0, finalString.lastIndexOf(delimiter));
    }
}
