import {Backend} from './backends/backend';
import {HttpBackend} from './backends/http_backend';
import {Application} from '../models/applications/application';
import {applicationPath} from '../js/config';


/**
 * Handles the configuration data retrieved from the orchestrator core.
 */
// TODO base service??
export class FeedbackService {
    private backend:Backend;

    /**
     * @param language
     *  Language to use when getting the data
     * @param backend
     *  Backend to get data from
     */
    constructor(apiEndpoint:string, language:string, backend?:Backend) {
        if(!backend) {
            this.backend = new HttpBackend(applicationPath, apiEndpoint, language);
        } else {
            this.backend = backend;
        }
    }

    sendFeedback(url: string, formData:FormData, callback:(application:Application) => void, errorCallback?:(data:any) => void) {
        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            dataType: 'json',
            processData: false,
            contentType: false,
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                errorCallback(data);
            }
        });
    }
}
