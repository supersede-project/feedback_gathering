define(["require", "exports", '../../services/mocks/mocks_loader', '../mechanisms/mechanism', '../mechanisms/rating_mechanism', '../../js/config', '../parameters/parameter_value_property_pair', '../mechanisms/screenshot_mechanism', './configuration_factory'], function (require, exports, mocks_loader_1, mechanism_1, rating_mechanism_1, config_1, parameter_value_property_pair_1, screenshot_mechanism_1, configuration_factory_1) {
    "use strict";
    describe('PushConfiguration object', function () {
        var configuration;
        beforeEach(function () {
            var applications = mocks_loader_1.readJSON('app/services/mocks/applications_mock.json', '/base/');
            var application = applications[0];
            var pushConfigurationData = application.configurations[0];
            configuration = configuration_factory_1.ConfigurationFactory.createByData(pushConfigurationData);
        });
        it('should be an object with a general configuration and some mechanisms', function () {
            expect(configuration.mechanisms.length).toBe(4);
            var textMechanismConfig = configuration.mechanisms[0];
            expect(textMechanismConfig.type).toEqual('TEXT_TYPE');
            expect(textMechanismConfig.active).toEqual(true);
            expect(textMechanismConfig.order).toEqual(1);
            expect(textMechanismConfig.canBeActivated).toEqual(false);
            var ratingMechanismConfig = configuration.mechanisms[3];
            expect(ratingMechanismConfig.type).toEqual('RATING_TYPE');
        });
        it('should have typed mechanism objects in its mechanism', function () {
            expect(configuration.mechanisms.length).toBe(4);
            var textMechanismConfig = configuration.mechanisms[0];
            expect(textMechanismConfig.type).toEqual('TEXT_TYPE');
            expect(textMechanismConfig).toEqual(jasmine.any(mechanism_1.Mechanism));
            var ratingMechanismConfig = configuration.mechanisms[3];
            expect(ratingMechanismConfig.type).toEqual('RATING_TYPE');
            expect(ratingMechanismConfig).toEqual(jasmine.any(rating_mechanism_1.RatingMechanism));
            var screenshotMechanismConfig = configuration.mechanisms[2];
            expect(screenshotMechanismConfig.type).toEqual('SCREENSHOT_TYPE');
            expect(screenshotMechanismConfig).toEqual(jasmine.any(screenshot_mechanism_1.ScreenshotMechanism));
        });
        it('should return the context for the view with the configuration data', function () {
            var context = configuration.getContextForView();
            var expectedContext = {
                textMechanism: {
                    active: true,
                    hint: 'Please enter your feedback',
                    label: 'Please write about your problem',
                    currentLength: 0,
                    maxLength: 200,
                    maxLengthVisible: 1,
                    textareaStyle: 'color: #7A7A7A;',
                    labelStyle: 'text-align: left; color: #353535; font-size: 15px;',
                    clearInput: 0,
                    mandatory: 1,
                    mandatoryReminder: 'Please fill in the text field',
                    validateOnSkip: 1
                },
                ratingMechanism: {
                    active: true,
                    title: 'Rate your user experience on this page'
                },
                screenshotMechanism: {
                    active: true
                },
                categoryMechanism: null,
                dialogId: 'pushConfiguration'
            };
            expect(context).toEqual(expectedContext);
        });
        it('should return the corresponding mechanisms', function () {
            var textMechanism = configuration.getMechanismConfig('TEXT_TYPE');
            expect(textMechanism).toEqual(jasmine.any(mechanism_1.Mechanism));
            expect(textMechanism).not.toBeNull();
            expect(textMechanism.type).toEqual('TEXT_TYPE');
            expect(textMechanism.active).toEqual(true);
            expect(textMechanism.order).toEqual(1);
            expect(textMechanism.canBeActivated).toEqual(false);
        });
        it('should return a rating mechanism object', function () {
            var ratingMechanism = configuration.getMechanismConfig('RATING_TYPE');
            expect(ratingMechanism).toEqual(jasmine.any(rating_mechanism_1.RatingMechanism));
            expect(ratingMechanism).not.toBeNull();
            expect(ratingMechanism.type).toEqual('RATING_TYPE');
            expect(ratingMechanism.active).toEqual(true);
            expect(ratingMechanism.order).toEqual(4);
            expect(ratingMechanism.canBeActivated).toEqual(false);
        });
        it('should return a css style string', function () {
            var textMechanism = configuration.getMechanismConfig(config_1.mechanismTypes.textType);
            var cssStyle = configuration.getCssStyle(textMechanism, [new parameter_value_property_pair_1.ParameterValuePropertyPair('textareaFontColor', 'color')]);
            expect(cssStyle).toEqual('color: #7A7A7A;');
            var cssStyle2 = configuration.getCssStyle(textMechanism, [new parameter_value_property_pair_1.ParameterValuePropertyPair('textareaFontColor', 'color'), new parameter_value_property_pair_1.ParameterValuePropertyPair('fieldFontType', 'font-style')]);
            expect(cssStyle2).toEqual('color: #7A7A7A; font-style: italic;');
        });
    });
});
//# sourceMappingURL=push_configuration.spec.js.map