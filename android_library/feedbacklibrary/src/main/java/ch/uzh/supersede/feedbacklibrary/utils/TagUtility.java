package ch.uzh.supersede.feedbacklibrary.utils;

import android.content.Context;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;

public class TagUtility {
    private TagUtility() {
    }
    public static Map<String,String> getFeedbackTags(Context context, List<String> loadedTags) {
        TreeMap<String,String> sortedTags = new TreeMap<>();
        for (String tag : loadedTags){
            sortedTags.put(tag.toLowerCase(),tag);
        }
        for (String tag : FeedbackDatabase
                .getInstance(context).readTags(null)){
            sortedTags.put(tag.toLowerCase(),tag);
        }
        return sortedTags;
    }

}
