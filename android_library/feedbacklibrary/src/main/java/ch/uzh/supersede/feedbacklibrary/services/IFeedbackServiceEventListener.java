package ch.uzh.supersede.feedbacklibrary.services;

public interface IFeedbackServiceEventListener {
    enum EventType {
        AUTHENTICATE,
        CREATE_USER,
        GET_USER,
        GET_USER_MOCK,
        CREATE_FEEDBACK,
        GET_FEEDBACK_LIST,
        GET_FEEDBACK_LIST_MOCK,
        GET_CONFIGURATION,
        GET_MINE_FEEDBACK_VOTES,
        GET_MINE_FEEDBACK_VOTES_MOCK,
        GET_OTHERS_FEEDBACK_VOTES,
        GET_OTHERS_FEEDBACK_VOTES_MOCK,
        GET_FEEDBACK_SUBSCRIPTIONS,
        GET_FEEDBACK_SUBSCRIPTIONS_MOCK,
        CREATE_SUBSCRIPTION,
        PING_REPOSITORY
    }

    void onEventCompleted(EventType eventType, Object response);

    void onEventFailed(EventType eventType, Object response);

    void onConnectionFailed(EventType eventType);
}
