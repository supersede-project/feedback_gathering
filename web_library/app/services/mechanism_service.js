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
        return MechanismService;
    }());
    exports.MechanismService = MechanismService;
});
