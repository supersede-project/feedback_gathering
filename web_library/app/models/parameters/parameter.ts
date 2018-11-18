import {ParameterInterface} from './parameter_interface';


export class Parameter implements ParameterInterface {
    id?:number;
    key:string;
    value:any;
    order?:number;

    constructor(id:number, key:string, value:any, order?:number) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    static initByData(data:any) {
        return new Parameter(data.id, data.key, data.value, data.order);
    }

}
