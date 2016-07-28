import {Backend} from './backend';


export class MockBackend implements Backend {
    private mockData:Array<any>;

    constructor(mockData:Array<any>) {
        this.mockData = mockData;
    }

    list():any {
        return this.mockData;
    }

    retrieve(id:number):any {
        return this.findById(id);
    }

    create(object:any):any {
        this.mockData.push(object);
        return object;
    }

    update(id:number, attributes:{}):any {
        var object = this.findById(id);
        for (let id in attributes) {
            if (attributes.hasOwnProperty(id)) {
                object[id] = attributes[id];
            }
        }
        return object;
    }

    destroy(id:number):any {
        var object = this.findById(id);
        let index = this.mockData.indexOf(object);
        if (index !== undefined) {
            this.mockData.splice(index, 1);
        }
        return object;
    }

    private findById(id:number): any {
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