define(["require", "exports"], function (require, exports) {
    "use strict";
    exports.apiEndpointOrchestrator = 'http://62.14.219.13:8080/';
    exports.applicationPath = 'feedback_orchestrator/{lang}/applications/';
    exports.applicationId = 4;
    exports.apiEndpointRepository = 'http://62.14.219.13:8080/';
    exports.feedbackPath = "feedback_repository/{lang}/feedbacks";
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
    exports.cookieNames = {
        lastTriggered: 'lastTriggered'
    };
});
//# sourceMappingURL=config.js.map