"use strict";
var jasmine_1 = require("jasmine");
var mechanism_service_1 = require('../../services/mechanism_service');
jasmine_1.describe("Mechanism Service", function () {
    var mechanismService;
    jasmine_1.beforeEach(function () {
        mechanismService = new mechanism_service_1.MechanismService();
    });
    jasmine_1.it("should return mechanism", function () {
        var mechanisms = mechanismService.findAll();
        jasmine_1.expect(mechanisms).toBeDefined();
    });
});
//# sourceMappingURL=mechanism_service.spec.js.map