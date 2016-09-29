export interface Backend {
    list(callback:(data:any) => void, errorCallback?:(data:any) => void):void;
    retrieve(id:number, callback:(data:any) => void, errorCallback?:(data:any) => void):void;
    create(object:any, callback?:(data:any) => void, errorCallback?:(data:any) => void):void;
    update(id:number, attributes:{}, callback?:(data:any) => void, errorCallback?:(data:any) => void):void;
    destroy(id:number, callback?:(data:any) => void, errorCallback?:(data:any) => void):void;
}