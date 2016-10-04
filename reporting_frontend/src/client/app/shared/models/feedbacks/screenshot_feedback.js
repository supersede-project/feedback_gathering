define(["require", "exports"], function (require, exports) {
    "use strict";
    var ScreenshotFeedback = (function () {
        function ScreenshotFeedback(name, mechanismId, part) {
            this.name = name;
            this.mechanismId = mechanismId;
            this.part = part;
        }
        return ScreenshotFeedback;
    }());
    exports.ScreenshotFeedback = ScreenshotFeedback;
});
//# sourceMappingURL=screenshot_feedback.js.map