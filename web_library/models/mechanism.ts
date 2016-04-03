import {Parameter} from './parameter';


export class Mechanism {
    type:string;
    parameters:Array<Parameter>;

    constructor(type:string, parameters:Array<Parameter>) {
        this.type = type;
        this.parameters = parameters;
    }
}