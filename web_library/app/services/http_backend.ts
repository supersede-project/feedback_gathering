import {Backend} from './backend';
import {apiEndpoint, orchestratorBasePath} from '../js/config';


export class HttpBackend implements Backend {
    private path:string;

    constructor(path:string) {
        this.path = path;
    }

    list():any {
        return null;
    }

    retrieve(id:number):any {
        return null;
    }

    create(object:any):any {
        return null;
    }

    update(id:number, attributes:{}):any {
        return null;
    }

    destroy(id:number):any {
        return null;
    }

    getUrl(): string {
        return apiEndpoint + orchestratorBasePath + this.path;
    }
}