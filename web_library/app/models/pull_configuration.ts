import {Mechanism} from './mechanism';
import {ParameterInterface} from './parameter_interface';


export class PullConfiguration {
    id:number;
    active:boolean;
    parameters:ParameterInterface[];
    mechanism:Mechanism[];

    constructor(id:number, active:boolean, parameters:ParameterInterface[], mechanism:Mechanism[]) {
        this.id = id;
        this.active = active;
        this.parameters = parameters;
        this.mechanism = mechanism;
    }
}