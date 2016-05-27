define(["require", "exports"], function (require, exports) {
    "use strict";
    var Rating = (function () {
        function Rating(title, rating) {
            this.title = title;
            this.rating = rating;
        }
        return Rating;
    }());
    exports.Rating = Rating;
});
//# sourceMappingURL=ratings.js.map