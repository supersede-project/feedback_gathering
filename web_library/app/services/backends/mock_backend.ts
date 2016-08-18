import {Backend} from './backend';


export class MockBackend implements Backend {
    private mockData:Array<any>;

    constructor(mockData:Array<any>) {
        this.mockData = mockData;
    }

    list(callback:(data:any) => void): void {
        callback(this.mockData);
    }

    retrieve(id:number, callback:(data:any) => void): void {
        callback(this.findById(id));
    }

    create(object:any, callback?:(data:any) => void): void {
        this.mockData.push(object);
        if(callback) {
            callback(object);
        }
    }

    update(id:number, attributes:{}, callback?:(data:any) => void): void {
        var object = this.findById(id);
        for (let id in attributes) {
            if (attributes.hasOwnProperty(id)) {
                object[id] = attributes[id];
            }
        }
        if(callback) {
            callback(object);
        }
    }

    destroy(id:number, callback?:(data:any) => void): void {
        var object = this.findById(id);
        let index = this.mockData.indexOf(object);
        if (index !== undefined) {
            this.mockData.splice(index, 1);
        }
        if(callback) {
            callback(object);
        }
    }

    private findById(id:number): void {
        var resultArray = this.mockData.filter(function (obj) {
            return obj.id === id;
        });
        if(resultArray === null || resultArray.length === 0) {
            return null;
        } else {
            return resultArray[0];
        }
    }
}