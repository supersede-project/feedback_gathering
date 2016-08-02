define(["require", "exports", '../../services/mocks/mocks_loader', './pull_configuration'], function (require, exports, mocks_loader_1, pull_configuration_1) {
    "use strict";
    describe('PullConfiguration object', function () {
        var pullConfiguration;
        beforeEach(function () {
            var configurations = mocks_loader_1.readJSON('app/services/mocks/configurations_mock.json', '/base/');
            var configurationData = configurations[0].pull_configurations[0];
            pullConfiguration = pull_configuration_1.PullConfiguration.initByData(configurationData);
        });
        it('should be an object with a complete pull configuration', function () {
            expect(pullConfiguration.active).toBeTruthy();
            expect(pullConfiguration.mechanisms.length).toBe(2);
            var textMechanismConfig = pullConfiguration.mechanisms[0];
            expect(textMechanismConfig.type).toEqual('TEXT_TYPE');
            expect(textMechanismConfig.active).toEqual(true);
            expect(textMechanismConfig.order).toEqual(1);
            expect(textMechanismConfig.canBeActivated).toEqual(false);
            var ratingMechanismConfig = pullConfiguration.mechanisms[1];
            expect(ratingMechanismConfig.type).toEqual('RATING_TYPE');
        });
        it('should return the context for the view with the configuration data', function () {
            var context = pullConfiguration.getContextForView();
            var expectedContext = {
                textMechanism: {
                    active: true,
                    hint: 'Please enter your feedback',
                    label: 'Feedback',
                    currentLength: 0,
                    maxLength: 50,
                    maxLengthVisible: 1,
                    textareaStyle: '',
                    labelStyle: 'text-align: left; font-size: 12px;',
                    clearInput: 0,
                    mandatory: 1,
                    mandatoryReminder: 'Please fill in the text field',
                    validateOnSkip: 1
                },
                ratingMechanism: {
                    active: true,
                    title: 'Rate your user experience'
                },
                screenshotMechanism: null,
                dialogId: 'pullConfiguration'
            };
            expect(context).toEqual(expectedContext);
        });
        it('should provide the parameter values', function () {
            var likelihood = pullConfiguration.getParameterValue("likelihood");
            var askOnAppStartup = pullConfiguration.getParameterValue("askOnAppStartup");
            expect(likelihood).toEqual(0.3);
            expect(askOnAppStartup).toEqual(0);
        });
    });
});
//# sourceMappingURL=pull_configuration.spec.js.map