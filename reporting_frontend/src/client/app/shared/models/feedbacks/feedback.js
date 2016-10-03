define(["require", "exports", '../../js/config'], function (require, exports, config_1) {
    "use strict";
    var validationMessages = {
        textMechanism: {
            noText: 'Please input a text'
        }
    };
    var Feedback = (function () {
        function Feedback(title, userIdentification, language, applicationId, configurationId, ratingFeedbacks, textFeedbacks, screenshotFeedbacks, categoryFeedbacks) {
            this.title = title;
            this.userIdentification = userIdentification;
            this.language = language;
            this.applicationId = applicationId;
            this.configurationId = configurationId;
            this.ratingFeedbacks = ratingFeedbacks;
            this.textFeedbacks = textFeedbacks;
            this.screenshotFeedbacks = screenshotFeedbacks;
            this.categoryFeedbacks = categoryFeedbacks;
        }
        Feedback.prototype.validate = function (configuration) {
            var textMechanisms = configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            var errors = { textMechanisms: [], ratingMechanisms: [], general: [] };
            this.validateTextMechanism(textMechanisms, errors);
            if (errors.textMechanisms.length === 0 && errors.ratingMechanisms.length === 0 && errors.general.length === 0) {
                return true;
            }
            else {
                return errors;
            }
        };
        Feedback.prototype.validateTextMechanism = function (textMechanisms, errors) {
            for (var _i = 0, textMechanisms_1 = textMechanisms; _i < textMechanisms_1.length; _i++) {
                var textMechanism = textMechanisms_1[_i];
                if (textMechanism) {
                    if (textMechanism.text === null || textMechanism.text === '') {
                        errors.textMechanism.push(validationMessages.textMechanism.noText);
                    }
                }
            }
        };
        return Feedback;
    }());
    exports.Feedback = Feedback;
});
//# sourceMappingURL=feedback.js.map