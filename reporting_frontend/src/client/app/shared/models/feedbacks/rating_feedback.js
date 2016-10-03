define(["require", "exports"], function (require, exports) {
    "use strict";
    var RatingFeedback = (function () {
        function RatingFeedback(rating, mechanismId) {
            this.rating = rating;
            this.mechanismId = mechanismId;
        }
        return RatingFeedback;
    }());
    exports.RatingFeedback = RatingFeedback;
});
//# sourceMappingURL=rating_feedback.js.map