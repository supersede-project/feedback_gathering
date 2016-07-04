define(["require", "exports"], function (require, exports) {
    "use strict";
    var ParameterPropertyPair = (function () {
        function ParameterPropertyPair(parameter, property) {
            this.parameter = parameter;
            this.property = property;
        }
        return ParameterPropertyPair;
    }());
    exports.ParameterPropertyPair = ParameterPropertyPair;
});
