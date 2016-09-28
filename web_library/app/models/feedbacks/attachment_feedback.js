define(["require", "exports"], function (require, exports) {
    "use strict";
    var AttachmentFeedback = (function () {
        function AttachmentFeedback(part, name, extension, mechanismId) {
            this.part = part;
            this.name = name;
            this.extension = extension;
            this.mechanismId = mechanismId;
        }
        return AttachmentFeedback;
    }());
    exports.AttachmentFeedback = AttachmentFeedback;
});
//# sourceMappingURL=attachment_feedback.js.map