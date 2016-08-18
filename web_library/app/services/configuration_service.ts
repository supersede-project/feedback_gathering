import {Backend} from './backends/backend';
import {HttpBackend} from './backends/http_backend';
import {PushConfiguration} from '../models/configurations/push_configuration';
import {ConfigurationInterface} from '../models/configurations/configuration_interface';
import {ConfigurationFactory} from '../models/configurations/configuration_factory';


/**
 * Handles the configuration data retrieved from the orchestrator core.
 */
export class ConfigurationService {
    private backend:Backend;

    /**
     * @param backend
     *  Backend to get data from
     */
    constructor(backend?: Backend) {
        if(!backend) {
            this.backend = new HttpBackend('feedback_orchestrator/example/configuration');
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
