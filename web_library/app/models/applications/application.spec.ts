import {readJSON} from '../../services/mocks/mocks_loader';
import {Application} from './application';
import {GeneralConfiguration} from '../configurations/general_configuration';
import {Parameter} from '../parameters/parameter';


describe('Application', () => {
    let application:Application;

    beforeEach(() => {
        var applications = readJSON('app/services/mocks/configurations_mock.json', '/base/');
        application = Application.initByData(applications[0]);
    });

    it('should have a general configuration', () => {
        var expectedParameters = [
            new Parameter(54, "reviewActive", 1),
            new Parameter(55, "mainColor", "#00ff00")
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