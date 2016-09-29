define(["require", "exports", '../../services/mocks/mocks_loader', './pull_configuration', './configuration_factory'], function (require, exports, mocks_loader_1, pull_configuration_1, configuration_factory_1) {
    "use strict";
    describe('PullConfiguration object', function () {
        var pullConfiguration;
        beforeEach(function () {
            var applications = mocks_loader_1.readJSON('app/services/mocks/test/applications_mock.json', '/base/');
            var application = applications[0];
            var pullConfigurationData = application.configurations[1];
            pullConfiguration = configuration_factory_1.ConfigurationFactory.createByData(pullConfigurationData);
        });
        it('should be an object with a complete pull configuration', function () {
            expect(pullConfiguration).toEqual(jasmine.any(pull_configuration_1.PullConfiguration));
            expect(pullConfiguration.id).toBe(2);
            expect(pullConfiguration.mechanisms.length).toBe(2);
            var textMechanismConfig = pullConfiguration.mechanisms[0];
            expect(textMechanismConfig.type).toEqual('TEXT_TYPE');
            expect(textMechanismConfig.active).toEqual(true);
            expect(textMechanismConfig.order).toEqual(1);
            expect(textMechanismConfig.canBeActivated).toEqual(false);
            var ratingMechanismConfig = pullConfiguration.mechanisms[1];
            expect(ratingMechanismConfig.type).toEqual('CATEGORY_TYPE');
        });
        it('should return the context for the view with the configuration data', function () {
            var context = pullConfiguration.getContextForView();
            expect(context.dialogId).toEqual('pullConfiguration');
            expect(context.mechanisms.length).toBe(2);
        });
        it('should provide the parameter values', function () {
            var likelihood = pullConfiguration.generalConfiguration.getParameterValue("likelihood");
            var askOnAppStartup = pullConfiguration.generalConfiguration.getParameterValue("askOnAppStartup");
            expect(likelihood).toEqual(0.1);
            expect(askOnAppStartup).toEqual(0);
        });
        it('should return whether the page slug matches in the config', function () {
            var pages = pullConfiguration.generalConfiguration.getParameterValue("pages");
            expect(pages).not.toBeNull();
            expect(pullConfiguration.pageDoesMatch('index.html')).toBeTruthy();
            expect(pullConfiguration.pageDoesMatch('info.html')).toBeTruthy();
            expect(pullConfiguration.pageDoesMatch('html')).toBeFalsy();
            expect(pullConfiguration.pageDoesMatch('info')).toBeFalsy();
            expect(pullConfiguration.pageDoesMatch('')).toBeFalsy();
            expect(pullConfiguration.pageDoesMatch('/')).toBeFalsy();
        });
        it('should return the slug', function () {
            expect(pullConfiguration.currentSlug()).toEqual('context.html');
        });
    });
});
//# sourceMappingURL=pull_configuration.spec.js.map