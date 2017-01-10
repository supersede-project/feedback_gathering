import {PushConfiguration} from './push_configuration';
import {readJSON} from '../../services/mocks/mocks_loader';
import {RatingMechanism} from '../mechanisms/rating_mechanism';
import {mechanismTypes} from '../../js/config';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';
import {ScreenshotMechanism} from '../mechanisms/screenshot_mechanism';
import {ConfigurationFactory} from './configuration_factory';
import {TextMechanism} from '../mechanisms/text_mechanism';


/*
describe('PushConfiguration object', () => {
    let configuration:PushConfiguration;

    beforeEach(() => {
        var applications = readJSON('app/services/mocks/test/applications_mock.json', '/base/');
        var application = applications[0];
        var pushConfigurationData = application.configurations[0];
        configuration = ConfigurationFactory.createByData(pushConfigurationData);
    });

    it('should be an object with a general configuration and some mechanisms', () => {
        expect(configuration.mechanisms.length).toBe(7);
        var textMechanismConfig = configuration.mechanisms[0];
        expect(textMechanismConfig.type).toEqual('TEXT_TYPE');
        expect(textMechanismConfig.active).toEqual(false);
        expect(textMechanismConfig.order).toEqual(1);
        expect(textMechanismConfig.canBeActivated).toEqual(false);

        var ratingMechanismConfig = configuration.mechanisms[3];
        expect(ratingMechanismConfig.type).toEqual('SCREENSHOT_TYPE');
    });

    it('should have typed mechanism objects in its mechanism', () => {
        expect(configuration.mechanisms.length).toBe(7);
        var textMechanismConfig = configuration.mechanisms[0];
        expect(textMechanismConfig.type).toEqual('TEXT_TYPE');
        expect(textMechanismConfig).toEqual(jasmine.any(TextMechanism));

        var ratingMechanismConfig = configuration.mechanisms[4];
        expect(ratingMechanismConfig.type).toEqual('RATING_TYPE');
        expect(ratingMechanismConfig).toEqual(jasmine.any(RatingMechanism));

        var screenshotMechanismConfig = configuration.mechanisms[3];
        expect(screenshotMechanismConfig.type).toEqual('SCREENSHOT_TYPE');
        expect(screenshotMechanismConfig).toEqual(jasmine.any(ScreenshotMechanism));
    });

    it('should return the context for the view with the configuration data', () => {
        var context = configuration.getContextForView();

        expect(context.dialogId).toEqual('pushConfiguration');
        expect(context.mechanisms.length).toBe(7);
    });

    it('should return the corresponding mechanisms', () => {
        var textMechanism = configuration.getMechanismConfig('TEXT_TYPE')[0];

        expect(textMechanism).toEqual(jasmine.any(TextMechanism));
        expect(textMechanism).not.toBeNull();

        expect(textMechanism.type).toEqual('TEXT_TYPE');
        expect(textMechanism.active).toEqual(false);
        expect(textMechanism.order).toEqual(1);
        expect(textMechanism.canBeActivated).toEqual(false);
    });

    it('should return a rating mechanism object', () => {
        var ratingMechanism = configuration.getMechanismConfig('RATING_TYPE')[0];

        expect(ratingMechanism).toEqual(jasmine.any(RatingMechanism));
        expect(ratingMechanism).not.toBeNull();

        expect(ratingMechanism.type).toEqual('RATING_TYPE');
        expect(ratingMechanism.active).toEqual(false);
        expect(ratingMechanism.order).toEqual(2);
        expect(ratingMechanism.canBeActivated).toEqual(false);
    });

    it('should return a css style string', () => {
        var textMechanism:TextMechanism = configuration.getMechanismConfig(mechanismTypes.textType)[0];

        var cssStyle = textMechanism.getCssStyle([new ParameterValuePropertyPair('textareaFontColor', 'color')]);
        expect(cssStyle).toEqual('color: #7A7A7A;');

        var cssStyle2 = textMechanism.getCssStyle(
            [new ParameterValuePropertyPair('textareaFontColor', 'color'), new ParameterValuePropertyPair('fieldFontType', 'font-style')]);
        expect(cssStyle2).toEqual('color: #7A7A7A; font-style: italic;');
    })
});
