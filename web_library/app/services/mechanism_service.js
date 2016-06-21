define(["require", "exports", '../models/mechanism'], function (require, exports, mechanism_1) {
    "use strict";
    var MechanismService = (function () {
        function MechanismService(data) {
            this.data = data;
        }
        MechanismService.prototype.getConfig = function () {
            var config = [];
            for (var i = 0; i < this.data.length; i++) {
                var element = this.data[i];
                if (element.hasOwnProperty('type')) {
                    var mechanism = this.getMechanismConfig(element['type']);
                    config.push(mechanism);
                }
            }
            return config;
        };
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
            var context = { textMechanism: null, ratingMechanism: null };
            var textMechanism = this.getMechanismConfig(mechanism_1.textType);
            var ratingMechanism = this.getMechanismConfig(mechanism_1.ratingType);
            if (textMechanism) {
                context.textMechanism = {
                    active: textMechanism.active,
                    hint: textMechanism.getParameter('hint').value,
                    currentLength: 0,
                    maxLength: textMechanism.getParameter('maxLength').value
                };
            }
            if (ratingMechanism) {
                context.ratingMechanism = {
                    active: ratingMechanism.active,
                    title: ratingMechanism.getParameter('title').value
                };
            }
            return context;
        };
        return MechanismService;
    }());
    exports.MechanismService = MechanismService;
});
