package ch.uzh.supersede.feedbacklibrary.services;

public interface IFeedbackServiceEventListener {
    enum EventType {
        PING_ORCHESTRATOR,
        PING_REPOSITORY,
        CREATE_FEEDBACK_VARIANT
    }

    void onEventCompleted(EventType eventType);

    void onEventFailed(EventType eventType);

    void onConnectionFailed(EventType eventType);
}
