import {readJSON} from '../../services/mocks/mocks_loader';
import {PullConfiguration} from './pull_configuration';
import {ConfigurationFactory} from './configuration_factory';


describe('PullConfiguration object', () => {
    let pullConfiguration:PullConfiguration;

    beforeEach(() => {
        var applications = readJSON('app/services/mocks/applications_mock.json', '/base/');
        var application = applications[0];

        var pullConfigurationData = application.configurations[1];
        pullConfiguration = <PullConfiguration>ConfigurationFactory.createByData(pullConfigurationData);
    });

    it('should be an object with a complete pull configuration', () => {
        expect(pullConfiguration).toEqual(jasmine.any(PullConfiguration));
        expect(pullConfiguration.id).toBe(2);

        expect(pullConfiguration.mechanisms.length).toBe(3);
        var textMechanismConfig = pullConfiguration.mechanisms[0];
        expect(textMechanismConfig.type).toEqual('TEXT_TYPE');
        expect(textMechanismConfig.active).toEqual(true);
        expect(textMechanismConfig.order).toEqual(1);
        expect(textMechanismConfig.canBeActivated).toEqual(false);

        var ratingMechanismConfig = pullConfiguration.mechanisms[1];
        expect(ratingMechanismConfig.type).toEqual('RATING_TYPE');
    });

    it('should return the context for the view with the configuration data', () => {
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
            screenshotMechanism: {
                active: true
            },
            dialogId: 'pullConfiguration'
        };

        expect(context).toEqual(expectedContext);
    });

    it('should provide the parameter values', () => {
        var likelihood = pullConfiguration.generalConfiguration.getParameterValue("likelihood");
        var askOnAppStartup = pullConfiguration.generalConfiguration.getParameterValue("askOnAppStartup");

        expect(likelihood).toEqual(0.4);
        expect(askOnAppStartup).toEqual(0);
    });
});
