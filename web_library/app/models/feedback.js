define(["require", "exports"], function (require, exports) {
    "use strict";
    var Feedback = (function () {
        function Feedback(title, application, user, text, configVersion, ratings) {
            this.title = title;
            this.application = application;
            this.user = user;
            this.text = text;
            this.configVersion = configVersion;
            this.ratings = ratings;
        }
        return Feedback;
    }());
    exports.Feedback = Feedback;
});
//# sourceMappingURL=feedback.js.map