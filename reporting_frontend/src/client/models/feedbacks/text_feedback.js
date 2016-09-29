define(["require", "exports"], function (require, exports) {
    "use strict";
    var TextFeedback = (function () {
        function TextFeedback(text, mechanismId) {
            this.text = text;
            this.mechanismId = mechanismId;
        }
        return TextFeedback;
    }());
    exports.TextFeedback = TextFeedback;
});
//# sourceMappingURL=text_feedback.js.map