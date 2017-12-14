import {Backend} from './backends/backend';
import {HttpBackend} from './backends/http_backend';
import {applicationPath} from '../js/config';
import {Feedback} from '../models/feedbacks/feedback';
import {FeedbackSettings} from "../models/feedbacks/feedback_settings";


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

    sendFeedback(url: string, formData:FormData, callback:(feedback:Feedback) => void, errorCallback?:(data:any) => void) {
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

    sendFeedbackSettings(url: string, formData:any, callback:(feedbackSettings:FeedbackSettings) => void, errorCallback?:(data:any) => void) {
        $.ajax({
            url: url,
            type: 'POST',
            data: JSON.stringify(formData),
            contentType: 'application/json',
            processData: false,
            // beforeSend:function(xhr){
            //     /* Authorization header */
            //     xhr.setRequestHeader("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXBlcl9hZG1pbiIsImNyZWF0ZWQiOjE1MTI4OTQ4NTI0MDMsImV4cCI6MTUxMzQ5OTY1Mn0.RRZ_OOM_39Lhl_jTpRBhlcQHix8KDVu0CF4EgPH84uTMCb4693Y88h29AnnV7B1mZMHfnvBHcf7tqCImfBJ6cQ");
            //     xhr.setRequestHeader("Cache-Control", "no-cache");
            // },
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                errorCallback(data);
            }
        });
    }
}
