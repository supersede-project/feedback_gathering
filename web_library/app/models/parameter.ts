import {ParameterInterface} from './parameter_interface';


export class Parameter implements ParameterInterface {
    key:string;
    value:any;

    constructor(key:string, value:any) {
        this.key = key;
        this.value = value;
    }
}
