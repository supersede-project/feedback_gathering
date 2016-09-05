import {ConfigurationService} from './configuration_service';
import {MockBackend} from './backends/mock_backend';
import {readJSON} from './mocks/mocks_loader';


describe('Configuration Service', () => {
    let configurationService:ConfigurationService;

    beforeEach(() => {
        var configurationMockData = readJSON('app/services/mocks/test/configurations_mock.json', '/base/');
        var backend = new MockBackend(configurationMockData);
        configurationService = new ConfigurationService(backend);
    });

    it('should retrieve the configuration from a backend', () => {
        configurationService.retrieveConfiguration(1, function(configuration) {
            expect(configuration.type).toEqual('PUSH');
        });
    });
});

