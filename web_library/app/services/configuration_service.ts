import {Backend} from './backends/backend';
import {HttpBackend} from './backends/http_backend';
import {PushConfiguration} from '../models/configurations/push_configuration';
import {ConfigurationInterface} from '../models/configurations/configuration_interface';
import {ConfigurationFactory} from '../models/configurations/configuration_factory';
import {apiEndpointOrchestrator} from '../js/config';


/**
 * Handles the configuration data retrieved from the orchestrator core.
 */
export class ConfigurationService {
    private backend:Backend;

    /**
     * @param language
     *  Language to use when getting the data
     * @param backend
     *  Backend to get data from
     */
    constructor(language:string, backend?: Backend) {
        if(!backend) {
            this.backend = new HttpBackend('feedback_orchestrator/example/configuration', apiEndpointOrchestrator, language);
        } else {
            this.backend = backend;
        }
    }

    retrieveConfiguration(configurationId:number, callback:(configuration:ConfigurationInterface) => void) {
        this.backend.retrieve(configurationId, function(configurationData:ConfigurationInterface) {
            var configurationObject = ConfigurationFactory.createByData(configurationData);
            callback(configurationObject);
        });
    }
}
