package ch.uzh.supersede.feedbacklibrary.utils;

public class CollectionUtility {

    private CollectionUtility() {
    }

    public static <T> T first(T[] array) {
        if (array != null && array.length > 0) {
            return array[0];
        }
        return null;
    }

    public static boolean oneOf(Object o, Object... objects){
        if (o == null || objects==null || objects.length == 0){
            return false;
        }
        for (Object o_ : objects){
            if (o.equals(o_)){
                return true;
            }
        }
        return false;
    }
}
