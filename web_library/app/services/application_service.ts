import {Backend} from './backends/backend';
import {HttpBackend} from './backends/http_backend';
import {Application} from '../models/applications/application';
import {apiEndpointOrchestrator, applicationPath} from '../js/config';


/**
 * Handles the configuration data retrieved from the orchestrator core.
 */
export class ApplicationService {
    private backend:Backend;

    /**
     * @param language
     *  Language to use when getting the data
     * @param backend
     *  Backend to get data from
     */
    constructor(language:string, backend?:Backend) {
        if(!backend) {
            this.backend = new HttpBackend(applicationPath, apiEndpointOrchestrator, language);
        } else {
            this.backend = backend;
        }
    }

    retrieveApplication(applicationId:number, callback:(application:Application) => void, errorCallback:(data:any) => void) {
        this.backend.retrieve(applicationId, applicationData => {
            var application = Application.initByData(applicationData);
            callback(application);
        }, data => {
            errorCallback(data);
        });
    }
}
