import {Configuration} from './configuration';
import {readJSON} from '../services/mocks/mocks_loader';
import {RatingMechanism} from './rating_mechanism';
import {Mechanism} from './mechanism';
import {textType} from '../js/config';
import {ParameterPropertyPair} from './parameter_property_pair';


describe('Configuration object', () => {
    let configuration:Configuration;

    beforeEach(() => {
        var configurations = readJSON('app/services/mocks/configurations_mock.json', '/base/');
        var configurationData = configurations[0];
        configuration = Configuration.initByData(configurationData);
    });

    it('should be an object with a complete configuration including general, pull and mechanism', () => {
        expect(configuration.mechanisms.length).toBe(4);
        var textMechanismConfig = configuration.mechanisms[0];
        expect(textMechanismConfig.type).toEqual('TEXT_TYPE');
        expect(textMechanismConfig.active).toEqual(true);
        expect(textMechanismConfig.order).toEqual(1);
        expect(textMechanismConfig.canBeActivated).toEqual(false);

        var ratingMechanismConfig = configuration.mechanisms[3];
        expect(ratingMechanismConfig.type).toEqual('RATING_TYPE');
    });

    it('should return the corresponding mechanisms', () => {
        var textMechanism = configuration.getMechanismConfig('TEXT_TYPE');

        expect(textMechanism).toEqual(jasmine.any(Mechanism));
        expect(textMechanism).not.toBeNull();

        expect(textMechanism.type).toEqual('TEXT_TYPE');
        expect(textMechanism.active).toEqual(true);
        expect(textMechanism.order).toEqual(1);
        expect(textMechanism.canBeActivated).toEqual(false);
    });

    it('should return a rating mechanism object', () => {
        var ratingMechanism = configuration.getMechanismConfig('RATING_TYPE');

        expect(ratingMechanism).toEqual(jasmine.any(RatingMechanism));
        expect(ratingMechanism).not.toBeNull();

        expect(ratingMechanism.type).toEqual('RATING_TYPE');
        expect(ratingMechanism.active).toEqual(true);
        expect(ratingMechanism.order).toEqual(4);
        expect(ratingMechanism.canBeActivated).toEqual(false);
    });

    it('should return the context for the view with the configuration data', () => {
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
            }
        };

        expect(context).toEqual(expectedContext);
    });

    it('should return a css style string', () => {
        var textMechanism = configuration.getMechanismConfig(textType);

        var cssStyle = configuration.getCssStyle(textMechanism, [new ParameterPropertyPair('textareaFontColor', 'color')]);
        expect(cssStyle).toEqual('color: #7A7A7A;');

        var cssStyle2 = configuration.getCssStyle(textMechanism,
            [new ParameterPropertyPair('textareaFontColor', 'color'), new ParameterPropertyPair('fieldFontType', 'font-style')]);
        expect(cssStyle2).toEqual('color: #7A7A7A; font-style: italic;');
    })
});