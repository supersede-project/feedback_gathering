import {Backend} from './backends/backend';
import {HttpBackend} from './backends/http_backend';
import {PushConfiguration} from '../models/configurations/push_configuration';


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

    retrieveConfiguration(callback:(configuration:PushConfiguration) => void) {
        this.backend.retrieve(1, function(configuration:PushConfiguration) {
            var configurationObject = PushConfiguration.initByData(configuration);
            callback(configurationObject);
        });
    }
}
