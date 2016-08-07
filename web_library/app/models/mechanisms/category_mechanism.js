var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './mechanism'], function (require, exports, mechanism_1) {
    "use strict";
    var CategoryMechanism = (function (_super) {
        __extends(CategoryMechanism, _super);
        function CategoryMechanism(id, type, active, order, canBeActivated, parameters) {
            _super.call(this, id, type, active, order, canBeActivated, parameters);
        }
        CategoryMechanism.prototype.getOptions = function () {
            return this.getParameterValue('options');
        };
        return CategoryMechanism;
    }(mechanism_1.Mechanism));
    exports.CategoryMechanism = CategoryMechanism;
});
//# sourceMappingURL=category_mechanism.js.map