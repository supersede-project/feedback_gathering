define(["require", "exports", '../../services/mocks/mocks_loader', './mechanism_factory'], function (require, exports, mocks_loader_1, mechanism_factory_1) {
    "use strict";
    describe('Category Mechanism', function () {
        var categoryMechanism;
        beforeEach(function () {
            var categoryMechanismData = mocks_loader_1.readJSON('app/services/mocks/category_mechanism_mock.json', '/base/');
            categoryMechanism = mechanism_factory_1.MechanismFactory.createByData(categoryMechanismData);
        });
        it('should return its options', function () {
            var options = categoryMechanism.getOptions();
            expect(options.length).toBe(3);
            expect(options[0].key).toEqual('BUG_CATEGORY');
            expect(options[0].value).toEqual('Bug');
            expect(options[1].key).toEqual('FEATURE_REQUEST_CATEGORY');
            expect(options[1].value).toEqual('Feature Request');
            expect(options[2].key).toEqual('GENERAL_CATEGORY');
            expect(options[2].value).toEqual('General Feedback');
        });
    });
});
//# sourceMappingURL=category_mechanism.spec.js.map