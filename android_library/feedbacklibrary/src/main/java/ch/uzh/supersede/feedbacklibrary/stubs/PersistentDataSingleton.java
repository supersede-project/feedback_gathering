package ch.uzh.supersede.feedbacklibrary.stubs;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.beans.*;

final class PersistentDataSingleton {
    private List<FeedbackDetailsBean> persistedFeedback = new ArrayList<>();
    private HashMap<Long,List<FeedbackResponseBean>> persistedFeedbackResponses = new HashMap<>();

    private static final PersistentDataSingleton ourInstance = new PersistentDataSingleton();

    static PersistentDataSingleton getInstance() {
        return ourInstance;
    }

    private PersistentDataSingleton() {
    }

    public List<FeedbackDetailsBean> getPersistedFeedback() {
        return persistedFeedback;
    }

    public void persistFeedbackBeans(List<FeedbackDetailsBean> feedbackBeans) {
        persistedFeedback = feedbackBeans;
    }

    public List<FeedbackResponseBean> getPersistedFeedbackResponses(long feedbackId) {
        if (persistedFeedbackResponses.containsKey(feedbackId)) {
            return persistedFeedbackResponses.get(feedbackId);
        }
        return new ArrayList<>();
    }

    public void persistFeedbackResponses(long feedbackId, List<FeedbackResponseBean> feedbackResponses) {
        persistedFeedbackResponses.put(feedbackId, feedbackResponses);
    }
}
