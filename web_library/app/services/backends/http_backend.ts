import {Backend} from './backend';
import {apiEndpoint} from '../../js/config';


export class HttpBackend implements Backend {
    private path:string;

    constructor(path:string) {
        this.path = path;
    }

    list(callback:(data:any) => void): void {
        var url = this.getUrl();
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

    retrieve(id:number, callback:(data:any) => void): void {
        return null;
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
        return apiEndpoint + this.path;
    }
}