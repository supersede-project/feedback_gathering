define(["require", "exports"], function (require, exports) {
    "use strict";
    var Category = (function () {
        function Category(mechanismId, text, categoryTypeId, categoryType) {
            this.mechanismId = mechanismId;
            this.text = text;
            this.categoryType = categoryType;
        }
        return Category;
    }());
    exports.Category = Category;
});
//# sourceMappingURL=category.js.map