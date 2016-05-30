define(["require", "exports", './mechanism_service'], function (require, exports, mechanism_service_1) {
    "use strict";
    describe('Mechanism Service', function () {
        var mechanismService;
        beforeEach(function () {
            mechanismService = new mechanism_service_1.MechanismService();
        });
        it('should return mechanisms', function () {
            var mechanisms = mechanismService.findAll();
            expect(mechanisms).toBeDefined();
        });
    });
});
//# sourceMappingURL=mechanism_service.spec.js.map