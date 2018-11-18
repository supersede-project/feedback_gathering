import {readJSON} from '../../services/mocks/mocks_loader';
import {Application} from './application';
import {GeneralConfiguration} from '../configurations/general_configuration';
import {Parameter} from '../parameters/parameter';


describe('Application', () => {
    let application:Application;

    beforeEach(() => {
        var applications = readJSON('app/services/mocks/test/applications_mock.json', '/base/');
        application = Application.initByData(applications[0]);
    });

    it('should have a general configuration', () => {
        var expectedParameters = [
            new Parameter(54, "reviewActive", 1),
            new Parameter(55, "mainColor", "#00ff00"),
            new Parameter(56, "dialogTitle", "Feedback")
        ];
        var expectedGeneralConfiguration = new GeneralConfiguration(1, "Senercon General Configuration", expectedParameters);

        expect(application.generalConfiguration).toEqual(expectedGeneralConfiguration);
    });

    it('should have multiple configurations', () => {
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

describe('Application with invalid data', () => {
    let application:Application;

    it('should create an application without configurations', () => {
        var applications = readJSON('app/services/mocks/test/application/application_without_configurations_mock.json', '/base/');
        application = Application.initByData(applications[0]);

        expect(application).toBeDefined();
        expect(application.configurations.length).toBe(0);
        expect(application.generalConfiguration).toBeDefined();

        const contextForView = application.getContextForView();
        expect(contextForView).toBeDefined();
    });

    it('should create an application without general configuration', () => {
        var applications = readJSON('app/services/mocks/test/application/application_without_general_configuration_mock.json', '/base/');
        application = Application.initByData(applications[0]);

        expect(application).toBeDefined();
        expect(application.configurations.length).toBe(3);
        expect(application.generalConfiguration).toBeNull();

        const contextForView = application.getContextForView();
        expect(contextForView).toBeDefined();
    });
});
