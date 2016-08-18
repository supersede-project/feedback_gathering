define(["require", "exports"], function (require, exports) {
    "use strict";
    exports.apiEndpointOrchestrator = 'http://localhost:8080/';
    exports.applicationPath = 'feedback_orchestrator/en/applications/';
    exports.applicationId = 13;
    exports.apiEndpointRepository = "http://localhost:8080/";
    exports.feedbackPath = "feedback_repository/de/feedbacks";
    exports.feedbackObjectTitle = 'Feedback';
    exports.applicationName = 'energiesparkonto.de';
    exports.defaultSuccessMessage = 'Your feedback was successfully sent';
    exports.dialogOptions = {
        autoOpen: false,
        height: 'auto',
        width: 'auto',
        minWidth: 500,
        modal: true,
        title: 'Feedback',
        buttons: {},
        resizable: false
    };
    exports.mechanismTypes = {
        textType: 'TEXT_TYPE',
        ratingType: 'RATING_TYPE',
        screenshotType: 'SCREENSHOT_TYPE',
        audioType: 'AUDIO_TYPE',
        categoryType: 'CATEGORY_TYPE',
        attachmentType: 'ATTACHMENT_TYPE'
    };
    exports.configurationTypes = {
        push: 'PUSH',
        pull: 'PULL'
    };
});
//# sourceMappingURL=config.js.map