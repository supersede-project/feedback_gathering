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
        return Mechanism;
    }());
    exports.Mechanism = Mechanism;
});
