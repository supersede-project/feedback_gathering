import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';


export class AttachmentMechanism extends Mechanism {
    dropArea:any;

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }

    getContext():any {
        return {
            title: this.getParameterValue('title')
        }
    }
}
