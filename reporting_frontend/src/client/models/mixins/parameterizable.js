define(["require", "exports"], function (require, exports) {
    "use strict";
    var Parameterizable = (function () {
        function Parameterizable() {
        }
        Parameterizable.prototype.getParameter = function (key) {
            var filteredArray = this.parameters.filter(function (parameter) { return parameter.key === key; });
            if (filteredArray.length > 0) {
                return filteredArray[0];
            }
            else {
                return null;
            }
        };
        Parameterizable.prototype.getParameterValue = function (key) {
            var parameter = this.getParameter(key);
            if (parameter === null || !parameter.hasOwnProperty('value')) {
                return null;
            }
            else {
                return parameter.value;
            }
        };
        return Parameterizable;
    }());
    exports.Parameterizable = Parameterizable;
});
//# sourceMappingURL=parameterizable.js.map