define(["require", "exports", './mechanism'], function (require, exports, mechanism_1) {
    "use strict";
    var validationMessages = {
        textMechanism: {
            noText: 'Please input a text'
        }
    };
    var Feedback = (function () {
        function Feedback(title, application, user, text, configVersion, ratings) {
            this.title = title;
            this.application = application;
            this.user = user;
            this.text = text;
            this.configVersion = configVersion;
            this.ratings = ratings;
        }
        Feedback.prototype.validate = function (mechanismService) {
            var textMechanism = mechanismService.getMechanismConfig(mechanism_1.textType);
            var ratingMechanism = mechanismService.getMechanismConfig(mechanism_1.ratingType);
            var errors = { textMechanism: [], ratingMechanism: [], general: [] };
            this.validateTextMechanism(textMechanism, errors);
            if (errors.textMechanism.length === 0 && errors.ratingMechanism.length === 0 && errors.general.length === 0) {
                return true;
            }
            else {
                return errors;
            }
        };
        Feedback.prototype.validateTextMechanism = function (textMechanism, errors) {
            if (textMechanism) {
                if (this.text === null || this.text === '') {
                    errors.textMechanism.push(validationMessages.textMechanism.noText);
                }
            }
        };
        return Feedback;
    }());
    exports.Feedback = Feedback;
});
