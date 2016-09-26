define(["require", "exports", '../../services/mocks/mocks_loader', './application', '../configurations/general_configuration', '../parameters/parameter'], function (require, exports, mocks_loader_1, application_1, general_configuration_1, parameter_1) {
    "use strict";
    describe('Application', function () {
        var application;
        beforeEach(function () {
            var applications = mocks_loader_1.readJSON('app/services/mocks/test/applications_mock.json', '/base/');
            application = application_1.Application.initByData(applications[0]);
        });
        it('should have a general configuration', function () {
            var expectedParameters = [
                new parameter_1.Parameter(54, "reviewActive", 1),
                new parameter_1.Parameter(55, "mainColor", "#00ff00"),
                new parameter_1.Parameter(56, "dialogTitle", "Feedback")
            ];
            var expectedGeneralConfiguration = new general_configuration_1.GeneralConfiguration(1, "Senercon General Configuration", expectedParameters);
            expect(application.generalConfiguration).toEqual(expectedGeneralConfiguration);
        });
        it('should have multiple configurations', function () {
            var configurations = application.configurations;
            expect(configurations.length).toBe(3);
            var pushConfiguration = application.getPushConfiguration();
            var pullConfiguration1 = application.getPullConfigurations()[0];
            var pullConfiguration2 = application.getPullConfigurations()[1];
            expect(pushConfiguration.type).toEqual('PUSH');
            expect(pullConfiguration1.type).toEqual('PULL');
            expect(pullConfiguration2.type).toEqual('PULL');
        });
    });
});
//# sourceMappingURL=application.spec.js.map