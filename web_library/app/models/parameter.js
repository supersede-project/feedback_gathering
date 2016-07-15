define(["require", "exports"], function (require, exports) {
    "use strict";
    var Parameter = (function () {
        function Parameter(key, value, editableByUser, defaultValue) {
            this.key = key;
            this.value = value;
            this.editableByUser = editableByUser;
            this.defaultValue = defaultValue;
        }
        return Parameter;
    }());
    exports.Parameter = Parameter;
});
//# sourceMappingURL=parameter.js.map