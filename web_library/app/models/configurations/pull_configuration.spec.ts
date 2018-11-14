import {readJSON} from '../../services/mocks/mocks_loader';
import {PullConfiguration} from './pull_configuration';
import {ConfigurationFactory} from './configuration_factory';
import {platform} from 'os';


describe('PullConfiguration object', () => {
    let pullConfiguration:PullConfiguration;

    beforeEach(() => {
        var applications = readJSON('app/services/mocks/test/applications_mock.json', '/base/');
        var application = applications[0];

        var pullConfigurationData = application.configurations[1];
        pullConfiguration = <PullConfiguration>ConfigurationFactory.createByData(pullConfigurationData);
    });

    it('should return the context for the application, the configuration itself and all the mechanisms', () => {
        expect(pullConfiguration.getContext()).toEqual(
            {
                dialogId: 'pullConfiguration',
                mechanisms: [
                    {
                        type: 'TEXT_TYPE',
                        order: 1,
                        canBeActivated: false,
                        page: 1,
                        cssClass: '',
                        active: true,
                        hint: 'Please enter your feedback',
                        label: 'Feedback',
                        currentLength: 0,
                        maxLength: 50,
                        maxLengthVisible: 1,
                        maxLengthStrict: null,
                        textareaStyle: 'border-color: #000000; border-width: 2px; background-color: #ffffff; height: 50px; width: 200px;',
                        labelStyle: 'text-align: left; font-size: 16px;',
                        clearInput: 0,
                        mandatory: 1,
                        mandatoryReminder: 'Please fill in the text field',
                        mechanismStyle: '',
                        boxWidth: '100%',
                        boxPaddingLeft: '0',
                        boxPaddingRight: '20px',
                        validationEmail: false
                    },
                    {
                        type: 'CATEGORY_TYPE',
                        order: 2,
                        canBeActivated: false,
                        page: 1,
                        cssClass: '',
                        active: true,
                        title: 'Please rate the feature that you just used',
                        ownAllowed: 0,
                        ownLabel: null,
                        multiple: 0,
                        labelStyle: '',
                        defaultOption: null,
                        asDropdown: false,
                        breakAfterOption: false,
                        options: [
                            {
                                key: 'RATING_1',
                                value: 'Very bad'
                            },
                            {
                                key: 'RATING_2',
                                value: 'Bad'
                            },
                            {
                                key: 'RATING_3',
                                value: 'Okay'
                            },
                            {
                                key: 'RATING_4',
                                value: 'Good'
                            },
                            {
                                key: 'RATING_5',
                                value: 'Very good'
                            }
                        ],
                        inputType: 'checkbox',
                        mandatory: null,
                        mandatoryReminder: null,
                        mechanismStyle: '',
                        boxWidth: '100%',
                        boxPaddingLeft: '0',
                        boxPaddingRight: '20px'
                    }
                ]
            }
        );
    });

    it('should be an object with a complete pull configuration', () => {
        expect(pullConfiguration).toEqual(jasmine.any(PullConfiguration));
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

    it('should return the context for the view with the configuration data', () => {
        var context = pullConfiguration.getContext();

        expect(context.dialogId).toEqual('pullConfiguration');
        expect(context.mechanisms.length).toBe(2)
    });

    it('should provide the parameter values', () => {
        var likelihood = pullConfiguration.generalConfiguration.getParameterValue("likelihood");
        var askOnAppStartup = pullConfiguration.generalConfiguration.getParameterValue("askOnAppStartup");

        expect(likelihood).toEqual(0.1);
        expect(askOnAppStartup).toEqual(0);
    });

    it('should return whether the page slug matches in the config', () => {
        var pages = pullConfiguration.generalConfiguration.getParameterValue("pages");

        expect(pages).not.toBeNull();

        expect(pullConfiguration.pageDoesMatch('index.html')).toBeTruthy();
        expect(pullConfiguration.pageDoesMatch('info.html')).toBeTruthy();
        expect(pullConfiguration.pageDoesMatch('html')).toBeFalsy();
        expect(pullConfiguration.pageDoesMatch('info')).toBeFalsy();
        expect(pullConfiguration.pageDoesMatch('')).toBeFalsy();
        expect(pullConfiguration.pageDoesMatch('/')).toBeFalsy();
    });

    it('should return the slug', () => {
        expect(pullConfiguration.currentSlug()).toEqual('context.html');
    });
});