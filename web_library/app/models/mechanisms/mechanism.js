define(["require", "exports", '../mixins/parameterizable', '../../js/helpers/mixin_helper'], function (require, exports, parameterizable_1, mixin_helper_1) {
    "use strict";
    var Mechanism = (function () {
        function Mechanism(id, type, active, order, canBeActivated, parameters) {
            this.id = id;
            this.type = type;
            this.active = active;
            this.order = order;
            this.canBeActivated = canBeActivated;
            this.parameters = parameters;
        }
        return Mechanism;
    }());
    exports.Mechanism = Mechanism;
    mixin_helper_1.applyMixins(Mechanism, [parameterizable_1.Parameterizable]);
});
//# sourceMappingURL=mechanism.js.map