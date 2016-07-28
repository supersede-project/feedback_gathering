import {ConfigurationService} from './configuration_service';
import {ParameterPropertyPair} from '../models/parameter_property_pair';
import {RatingMechanism} from '../models/rating_mechanism';
import {textType} from '../js/config';
import {Mechanism} from '../models/mechanism';
import {MockBackend} from './backends/mock_backend';
import {readJSON} from './mocks/mocks_loader';


describe('Configuration Service', () => {
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

