import {ConfigurationService} from './configuration_service';
import {MockBackend} from './backends/mock_backend';
import {readJSON} from './mocks/mocks_loader';


describe('PushConfiguration Service', () => {
    let configurationService:ConfigurationService;

    beforeEach(() => {
        var configurationMockData = readJSON('app/services/mocks/configurations_mock.json');
        var backend = new MockBackend(configurationMockData);
        configurationService = new ConfigurationService(backend);
    });

    it('should have the correct backend object', () => {

    });

    it('should retrieve the configuration from a backend', () => {

    });
});

