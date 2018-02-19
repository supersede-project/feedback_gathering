import {Application} from '../models/applications/application';
import {applicationPath} from '../js/config';


/**
 * Handles the configuration data retrieved from the orchestrator core.
 */
// TODO all the services need to be rewritten, since url building is not elegant enough
export class EndUserApplicationService {
    private apiEndpoint:string;
    private language:string;

    /**
     * @param apiEndpoint
     *  Base URL to the backend service
     * @param language
     *  Language to use when getting the data
     */
    constructor(apiEndpoint:string, language:string) {
        this.apiEndpoint = apiEndpoint;
        this.language = language;
    }

    retrieveApplicationForEndUser(applicationId:number, userIdentification:string, callback:(application:Application) => void, errorCallback?:(data:any) => void) {
        let url = this.buildUrl(applicationId, userIdentification);

        jQuery.ajax({
            url: url,
            crossDomain: true,
            dataType: 'json',
            cache: false,
            contentType: "application/json; charset=utf-8;",
            type: 'GET',
            success: function (data) {
                let application = Application.initByData(data);
                callback(application);
            },
            error: function (data) {
                errorCallback(data);
            }
        });
    }

    private buildUrl(applicationId:number, userIdentification:string):string {
        let url = (this.apiEndpoint + applicationPath).replace('{lang}', this.language);
        url += String(applicationId) + "/user_identification/" + userIdentification;
        return url;
    }
}