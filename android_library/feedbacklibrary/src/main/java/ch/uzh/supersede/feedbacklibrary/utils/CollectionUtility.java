package ch.uzh.supersede.feedbacklibrary.utils;

import java.lang.reflect.Array;
import java.util.List;

public final class CollectionUtility {

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

    @SuppressWarnings("unchecked")
    public static  <T> T[] subArray(Class<T> clazz, List<T> source, int begin, int end){
        if (source.size() <= end || end <= begin){
            return (T[]) Array.newInstance(clazz, 0);
        }
        T[] array = (T[]) Array.newInstance(clazz, end-begin);
        for (int i = begin; i < end; i++){
            array[i] = source.get(i);
        }
        return array;
    }
}
