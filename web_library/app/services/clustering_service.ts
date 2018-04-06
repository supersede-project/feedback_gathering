export class ClusteringService {

    constructor() {}

    /**
     *
     * @param {string} url
     * @param {string} feedbackText
     * @param {string} tenant, now 'atos' or 'senercon'
     * @param {number} numberOfResults
     * @param callback
     * @param errorCallback
     */
    retrieveRelatedFeedback(url: string, feedbackText:string, tenant:string, numberOfResults:number,
                            callback:(data:any) => void, errorCallback?:(data:any) => void) {
        let body = {
            'feedback': feedbackText,
            'tenant': tenant,
            'N': numberOfResults
        };

        $.ajax({
            url: url,
            type: 'POST',
            data: body,
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
