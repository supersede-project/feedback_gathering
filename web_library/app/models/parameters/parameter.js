define(["require", "exports"], function (require, exports) {
    "use strict";
    var Parameter = (function () {
        function Parameter(id, key, value) {
            this.id = id;
            this.key = key;
            this.value = value;
        }
        Parameter.initByData = function (data) {
            return new Parameter(data.id, data.key, data.value);
        };
        return Parameter;
    }());
    exports.Parameter = Parameter;
});
//# sourceMappingURL=parameter.js.map