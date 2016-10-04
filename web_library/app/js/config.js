define(["require", "exports"], function (require, exports) {
    "use strict";
    exports.apiEndpointOrchestrator = 'https://ptvfeedback.ronnieschaniel.com/';
    exports.applicationPath = 'feedback_orchestrator/{lang}/applications/';
    exports.applicationId = 8;
    exports.apiEndpointRepository = 'https://ptvfeedback.ronnieschaniel.com/';
    exports.feedbackPath = 'feedback_repository/{lang}/feedbacks';
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
    exports.cookieNames = {
        lastTriggered: 'lastTriggered'
    };
});
//# sourceMappingURL=config.js.map