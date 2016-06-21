define(["require", "exports", '../models/mechanism'], function (require, exports, mechanism_1) {
    "use strict";
    var MechanismService = (function () {
        function MechanismService(data) {
            this.data = data;
        }
        MechanismService.prototype.getMechanismConfig = function (mechanismTypeConstant) {
            var filteredArray = this.data.filter(function (mechanism) { return mechanism.type === mechanismTypeConstant; });
            if (filteredArray.length > 0) {
                return mechanism_1.Mechanism.initByData(filteredArray[0]);
            }
            else {
                return null;
            }
        };
        MechanismService.prototype.getContextForView = function () {
            var textMechanism = this.getMechanismConfig(mechanism_1.textType);
            var ratingMechanism = this.getMechanismConfig(mechanism_1.ratingType);
            return {
                textMechanism: {
                    active: textMechanism.active,
                    hint: textMechanism.getParameter('hint').value,
                    currentLength: 0,
                    maxLength: textMechanism.getParameter('maxLength').value
                },
                ratingMechanism: {
                    active: ratingMechanism.active,
                    title: ratingMechanism.getParameter('title').value
                }
            };
        };
        return MechanismService;
    }());
    exports.MechanismService = MechanismService;
});
