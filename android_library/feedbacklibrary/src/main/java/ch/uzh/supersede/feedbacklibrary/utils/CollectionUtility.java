package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.interfaces.ISortableFeedback;

public class CollectionUtility {

    private CollectionUtility(){
    }

    public static  <T> T first(T[] array){
        if (array != null && array.length>0){
            return array[0];
        }
        return null;
    }
}
