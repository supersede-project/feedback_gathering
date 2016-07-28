define(["require", "exports", './rating_mechanism', '../js/config', './mechanism'], function (require, exports, rating_mechanism_1, config_1, mechanism_1) {
    "use strict";
    var MechanismFactory = (function () {
        function MechanismFactory() {
        }
        MechanismFactory.createByData = function (data) {
            if (!data.hasOwnProperty('type') || !data.hasOwnProperty('active')) {
                return null;
            }
            if (data.type === config_1.ratingType) {
                return new rating_mechanism_1.RatingMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, data.parameters);
            }
            else {
                return new mechanism_1.Mechanism(data.id, data.type, data.active, data.order, data.canBeActivated, data.parameters);
            }
        };
        return MechanismFactory;
    }());
    exports.MechanismFactory = MechanismFactory;
});
//# sourceMappingURL=mechanism_factory.js.map