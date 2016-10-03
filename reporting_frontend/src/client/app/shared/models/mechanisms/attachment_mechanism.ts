import {Mechanism} from './mechanism';
import {Parameter} from '../parameters/parameter';


export class AttachmentMechanism extends Mechanism {

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }
}
