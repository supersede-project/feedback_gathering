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
        var context = pullConfiguration.getContextForView();

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


