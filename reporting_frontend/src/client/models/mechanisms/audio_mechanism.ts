import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';


export class AudioMechanism extends Mechanism {

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }
}
