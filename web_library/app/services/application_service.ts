import {Backend} from './backends/backend';
import {HttpBackend} from './backends/http_backend';
import {Application} from '../models/applications/application';
import {apiEndpointOrchestrator} from '../js/config';


/**
 * Handles the configuration data retrieved from the orchestrator core.
 */
export class ApplicationService {
    private backend:Backend;

    /**
     * @param backend
     *  Backend to get data from
     */
    constructor(backend?: Backend) {
        if(!backend) {
            this.backend = new HttpBackend('feedback_orchestrator/example/configuration', apiEndpointOrchestrator);
        } else {
            this.backend = backend;
        }
    }

    retrieveApplication(applicationId:number, callback:(application:Application) => void) {
        this.backend.retrieve(applicationId, function(applicationData) {
            var application = Application.initByData(applicationData);
            callback(application);
        });
    }
}
