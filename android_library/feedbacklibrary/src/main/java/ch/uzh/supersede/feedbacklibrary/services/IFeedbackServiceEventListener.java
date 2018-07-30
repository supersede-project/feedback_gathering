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
        GET_FEEDBACK_LIST_PRIVATE,
        GET_CONFIGURATION,
        GET_FEEDBACK_LIST_VOTED,
        GET_FEEDBACK_LIST_VOTED_MOCK,
        GET_FEEDBACK_LIST_OWN,
        GET_FEEDBACK_LIST_OWN_MOCK,
        GET_FEEDBACK_LIST_SUBSCRIBED,
        GET_FEEDBACK_LIST_SUBSCRIBED_MOCK,
        GET_FEEDBACK_IMAGE,
        GET_FEEDBACK_IMAGE_MOCK,
        GET_TAG_LIST,
        GET_TAG_LIST_MOCK,
        PING_REPOSITORY,
        PING_REPOSITORY_MOCK,
        CREATE_FEEDBACK_SUBSCRIPTION,
        CREATE_FEEDBACK_SUBSCRIPTION_MOCK,
        CREATE_FEEDBACK_RESPONSE,
        CREATE_FEEDBACK_RESPONSE_MOCK,
        GET_FEEDBACK_REPORT_LIST,
        DELETE_FEEDBACK,
        DELETE_FEEDBACK_MOCK,
        CREATE_FEEDBACK_REPORT,
        CREATE_FEEDBACK_REPORT_MOCK,
        CREATE_VOTE,
        CREATE_VOTE_MOCK,
        EDIT_FEEDBACK_PUBLICATION,
        EDIT_FEEDBACK_PUBLICATION_MOCK,
        EDIT_FEEDBACK_STATUS,
        EDIT_FEEDBACK_STATUS_MOCK
    }

    void onEventCompleted(EventType eventType, Object response);

    void onEventFailed(EventType eventType, Object response);

    void onConnectionFailed(EventType eventType);
}
