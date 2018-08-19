package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.List;

public final class StringUtility {

    private StringUtility() {
    }

    public static boolean hasText(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean equals(String a, String b) {
        return hasText(a) && hasText(b) && a.equals(b);
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

    public static String join(long[] array, String separator) {
        return join(ObjectUtility.asList(array), separator);
    }

    public static String join(int[] array, String separator) {
        return join(ObjectUtility.asList(array), separator);
    }

    public static String join(char[] array, String separator) {
        return join(ObjectUtility.asList(array), separator);
    }

    public static String join(boolean[] array, String separator) {
        return join(ObjectUtility.asList(array), separator);
    }

    public static String join(double[] array, String separator) {
        return join(ObjectUtility.asList(array), separator);
    }

    public static String join(float[] array, String separator) {
        return join(ObjectUtility.asList(array), separator);
    }

    public static String join(short[] array, String separator) {
        return join(ObjectUtility.asList(array), separator);
    }

    private static <T> String join(List<T> items, String separator){
        StringBuilder sb = new StringBuilder();
        if (items.size() > 1) {
            for (int i = 0; i < items.size() - 1; i++) {
                sb.append(items.get(i));
                sb.append(separator);
            }
        }
        if (!items.isEmpty()){
            sb.append(items.get(items.size() - 1));
        }
        return sb.toString();
    }


    public static String generateSpace(int i) {
        StringBuilder s = new StringBuilder();
        for (int e = 0; e < i; e++){
            s.append(" ");
        }
        return s.toString();
    }

    public static String nulLSafe(String str) {
        return str==null?"":str;
    }

    public static boolean contains(String text, String... tokens) {
        if (text == null || tokens == null){
            return false;
        }
        for (String token : tokens){
            if (!text.contains(token)){
                return false;
            }
        }
        return true;
    }
}
