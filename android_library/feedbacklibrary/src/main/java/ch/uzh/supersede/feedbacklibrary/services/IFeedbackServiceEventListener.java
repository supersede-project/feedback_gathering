package ch.uzh.supersede.feedbacklibrary.services;

public interface IFeedbackServiceEventListener {
    enum EventType {
        CREATE_FEEDBACK_VARIANT,
        GET_CONFIGURATION,
        GET_MINE_FEEDBACK_VOTES,
        GET_OTHERS_FEEDBACK_VOTES,
        GET_FEEDBACK_SETTINGS,
        CREATE_SUBSCRIPTION
    }

    void onEventCompleted(EventType eventType, Object response);

    void onEventFailed(EventType eventType, Object response);

    void onConnectionFailed(EventType eventType);
}
