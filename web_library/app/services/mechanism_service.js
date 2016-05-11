"use strict";
var mechanism_1 = require('../models/mechanism');
var MechanismService = (function () {
    function MechanismService() {
    }
    MechanismService.prototype.findAll = function () {
        var mechanisms = [
            new mechanism_1.Mechanism('TEXT_MECHANISM', true),
            new mechanism_1.Mechanism('SCREENSHOT_MECHANISM', true)
        ];
        return mechanisms;
    };
    return MechanismService;
}());
exports.MechanismService = MechanismService;
//# sourceMappingURL=mechanism_service.js.map