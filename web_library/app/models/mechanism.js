define(["require", "exports"], function (require, exports) {
    "use strict";
    exports.textType = 'TEXT_TYPE';
    exports.ratingType = 'RATING_TYPE';
    exports.screenShotType = 'SCREEN_SHOT_TYPE';
    var Mechanism = (function () {
        function Mechanism(type, active, order, canBeActivated, parameters) {
            this.type = type;
            this.active = active;
            this.order = order;
            this.canBeActivated = canBeActivated;
            this.parameters = parameters;
        }
        Mechanism.initByData = function (data) {
            if (data.type === null || data.active === null) {
                return null;
            }
            return new Mechanism(data.type, data.active, data.order, data.canBeActivated, data.parameters);
        };
        Mechanism.prototype.getParameter = function (key) {
            var filteredArray = this.parameters.filter(function (parameter) { return parameter.key === key; });
            if (filteredArray.length > 0) {
                return filteredArray[0];
            }
            else {
                return null;
            }
        };
        return Mechanism;
    }());
    exports.Mechanism = Mechanism;
});
