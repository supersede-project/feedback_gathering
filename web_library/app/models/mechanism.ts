import {Parameter} from './parameter';


export class Mechanism {
    type:string;
    active:boolean;
    order: number;
    canBeActivated:boolean;
    parameters: Parameter[];

    constructor(type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:Parameter[]) {
        this.type = type;
        this.active = active;
        this.order = order;
        this.canBeActivated = canBeActivated;
        this.parameters = parameters;
    }
}

