import {Mechanism} from './mechanism';
import {ParameterInterface} from '../parameters/parameter_interface';


export class TextMechanism extends Mechanism {

    constructor(id:number, type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:ParameterInterface[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }
}
