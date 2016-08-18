define(["require", "exports"], function (require, exports) {
    "use strict";
    exports.apiEndpointOrchestrator = 'http://ec2-54-166-31-250.compute-1.amazonaws.com/';
    exports.applicationPath = 'feedback_orchestrator/en/applications/';
    exports.applicationId = 8;
    exports.apiEndpointRepository = "http://ec2-54-166-31-250.compute-1.amazonaws.com/";
    exports.feedbackPath = "feedback_repository/de/feedbacks";
    exports.feedbackObjectTitle = 'Feedback';
    exports.applicationName = 'PTV';
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