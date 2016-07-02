define(["require", "exports"], function (require, exports) {
    "use strict";
    var Mechanism = (function () {
        function Mechanism(type, active, order, canBeActivated, parameters) {
            this.type = type;
            this.active = active;
            this.order = order;
            this.canBeActivated = canBeActivated;
            this.parameters = parameters;
        }
        Mechanism.prototype.getParameter = function (key) {
            var filteredArray = this.parameters.filter(function (parameter) { return parameter.key === key; });
            if (filteredArray.length > 0) {
                return filteredArray[0];
            }
            else {
                return null;
            }
        };
        Mechanism.prototype.getParameterValue = function (key) {
            var parameter = this.getParameter(key);
            if (parameter == null || !parameter.hasOwnProperty('value')) {
                return null;
            }
            else {
                return parameter.value;
            }
        };
        return Mechanism;
    }());
    exports.Mechanism = Mechanism;
});
