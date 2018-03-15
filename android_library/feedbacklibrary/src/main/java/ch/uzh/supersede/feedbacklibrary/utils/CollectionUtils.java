package ch.uzh.supersede.feedbacklibrary.utils;

public class CollectionUtils {
    public static  <T> T first(T[] array){
        if (array != null && array.length>0){
            return array[0];
        }
        return null;
    }
}
