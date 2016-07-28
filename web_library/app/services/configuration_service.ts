import {Backend} from './backends/backend';
import {HttpBackend} from './backends/http_backend';
import {Configuration} from '../models/configuration';


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

    retrieveConfiguration(callback:(configuration:Configuration) => void) {
        this.backend.retrieve(1, function(configuration:Configuration) {
            var configurationObject = Configuration.initByData(configuration);
            callback(configurationObject);
        });
    }
}
