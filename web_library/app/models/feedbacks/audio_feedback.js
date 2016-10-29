define(["require", "exports"], function (require, exports) {
    "use strict";
    var AudioFeedback = (function () {
        function AudioFeedback(part, duration, fileExtension, mechanismId) {
            this.part = part;
            this.duration = duration;
            this.fileExtension = fileExtension;
            this.mechanismId = mechanismId;
            this.name = part;
        }
        return AudioFeedback;
    }());
    exports.AudioFeedback = AudioFeedback;
});
//# sourceMappingURL=audio_feedback.js.map