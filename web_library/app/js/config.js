define(["require", "exports"], function (require, exports) {
    "use strict";
    exports.apiEndpoint = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/';
    exports.orchestratorBasePath = 'feedback_orchestrator/example/';
    exports.configPath = 'feedback_orchestrator/example/configuration';
    exports.feedbackPath = 'feedback_repository/example/feedback';
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
    exports.textType = 'TEXT_TYPE';
    exports.ratingType = 'RATING_TYPE';
    exports.screenshotType = 'SCREENSHOT_TYPE';
});
//# sourceMappingURL=config.js.map