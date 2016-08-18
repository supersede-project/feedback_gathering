import {ParameterInterface} from './parameter_interface';


export class Parameter implements ParameterInterface {
    id:number;
    key:string;
    value:any;

    constructor(id:number, key:string, value:any) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    static initByData(data:any) {
        return new Parameter(data.id, data.key, data.value);
    }

}
