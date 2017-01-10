import {readJSON} from '../../services/mocks/mocks_loader';
import {PullConfiguration} from './pull_configuration';
import {Application} from '../applications/application';
import {ConfigurationFactory} from './configuration_factory';
import {PushConfiguration} from './push_configuration';


describe('Configuration factory', () => {
    let application:Application;

    beforeEach(() => {
        var applications = readJSON('app/services/mocks/test/applications_mock.json', '/base/');
        application = applications[0];
    });

    it('should return the correct configuration object depending on the type attribute', () => {
        var configuration1 = ConfigurationFactory.createByData(application.configurations[0]);
        var configuration2:PullConfiguration = <PullConfiguration>ConfigurationFactory.createByData(application.configurations[1]);

        expect(configuration1.dialogId).toEqual('pushConfiguration');
        expect(configuration2.shouldGetTriggered()).toBeDefined();
        expect(configuration2.dialogId).toEqual('pullConfiguration');

        expect(configuration1).toEqual(jasmine.any(PushConfiguration));
        expect(configuration2).toEqual(jasmine.any(PullConfiguration));
    });
});