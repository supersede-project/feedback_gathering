define(["require", "exports", './rating_mechanism', './mechanism', '../../js/config', './screenshot_mechanism', '../parameters/parameter', './category_mechanism'], function (require, exports, rating_mechanism_1, mechanism_1, config_1, screenshot_mechanism_1, parameter_1, category_mechanism_1) {
    "use strict";
    var MechanismFactory = (function () {
        function MechanismFactory() {
        }
        MechanismFactory.createByData = function (data) {
            if (!data.hasOwnProperty('type')) {
                return null;
            }
            var parameters = [];
            for (var _i = 0, _a = data.parameters; _i < _a.length; _i++) {
                var parameter = _a[_i];
                parameters.push(parameter_1.Parameter.initByData(parameter));
            }
            if (data.type === config_1.mechanismTypes.ratingType) {
                return new rating_mechanism_1.RatingMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
            }
            else if (data.type === config_1.mechanismTypes.screenshotType) {
                return new screenshot_mechanism_1.ScreenshotMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
            }
            else if (data.type === config_1.mechanismTypes.categoryType) {
                return new category_mechanism_1.CategoryMechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
            }
            else {
                return new mechanism_1.Mechanism(data.id, data.type, data.active, data.order, data.canBeActivated, parameters);
            }
        };
        return MechanismFactory;
    }());
    exports.MechanismFactory = MechanismFactory;
});
//# sourceMappingURL=mechanism_factory.js.map