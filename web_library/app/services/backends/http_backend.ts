import {Backend} from './backend';


export class HttpBackend implements Backend {
    private path:string;
    private apiEndpoint:string;
    private language:string;
    private url:string;

    constructor(path:string, apiEndpoint:string, language:string) {
        this.path = path;
        this.apiEndpoint = apiEndpoint;
        this.language = language;
        this.url = (this.apiEndpoint + this.path).replace('{lang}', this.language);
    }

    list(callback:(data:any) => void, errorCallback?:(data:any) => void): void {
        var url = this.getUrl();
        jQuery.ajax({
            url: url,
            dataType: 'json',
            type: 'GET',
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                errorCallback(data);
            }
        });
    }

    retrieve(id:number, callback:(data:any) => void, errorCallback?:(data:any) => void): void {
        var url = this.getUrl() + id;
        jQuery.ajax({
            url: url,
            crossDomain: true,
            dataType: 'json',
            cache: false,
            contentType: "application/json; charset=utf-8;",
            type: 'GET',
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                errorCallback(data);
            }
        });
    }

    create(object:any, callback?:(data:any) => void, errorCallback?:(data:any) => void): void {
        return null;
    }

    update(id:number, attributes:{}, callback?:(data:any) => void, errorCallback?:(data:any) => void): void {
        return null;
    }

    destroy(id:number, callback?:(data:any) => void, errorCallback?:(data:any) => void): void {
        return null;
    }

    setUrl(url:string) {
        this.url = url;
    }

    getUrl(): string {
        return this.url;
    }
}