import {Backend} from './backend';


export class HttpBackend implements Backend {
    private path:string;
    private apiEndpoint:string;

    constructor(path:string, apiEndpoint:string) {
        this.path = path;
        this.apiEndpoint = apiEndpoint;
    }

    list(callback:(data:any) => void): void {
        var url = this.getUrl();
        jQuery.ajax({
            url: url,
            dataType: 'json',
            type: 'GET',
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                callback(data);
            }
        });
    }

    retrieve(id:number, callback:(data:any) => void): void {
        var url = this.getUrl() + id;

        jQuery.ajax({
            url: url,
            type: 'GET',
            success: function (data) {
                callback(data);
            },
            error: function (data) {
                callback(data);
            }
        });
    }

    create(object:any, callback?:(data:any) => void): void {
        return null;
    }

    update(id:number, attributes:{}, callback?:(data:any) => void): void {
        return null;
    }

    destroy(id:number, callback?:(data:any) => void): void {
        return null;
    }

    getUrl(): string {
        return this.apiEndpoint + this.path;
    }
}