define(["require", "exports", '../../services/mocks/mocks_loader', './pull_configuration', './configuration_factory', './push_configuration'], function (require, exports, mocks_loader_1, pull_configuration_1, configuration_factory_1, push_configuration_1) {
    "use strict";
    describe('Configuration factory', function () {
        var application;
        beforeEach(function () {
            var applications = mocks_loader_1.readJSON('app/services/mocks/test/applications_mock.json', '/base/');
            application = applications[0];
        });
        it('should return the correct configuration object depending on the type attribute', function () {
            var configuration1 = configuration_factory_1.ConfigurationFactory.createByData(application.configurations[0]);
            var configuration2 = configuration_factory_1.ConfigurationFactory.createByData(application.configurations[1]);
            expect(configuration1.dialogId).toEqual('pushConfiguration');
            expect(configuration2.shouldGetTriggered()).toBeDefined();
            expect(configuration2.dialogId).toEqual('pullConfiguration');
            expect(configuration1).toEqual(jasmine.any(push_configuration_1.PushConfiguration));
            expect(configuration2).toEqual(jasmine.any(pull_configuration_1.PullConfiguration));
        });
    });
});
//# sourceMappingURL=configuration_factory.spec.js.map