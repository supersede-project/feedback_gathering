import {Mechanism} from './mechanism';
import {Parameter} from '../parameters/parameter';


export class AttachmentMechanism extends Mechanism {
    dropArea:any;

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }

    getContext():any {
        return {
            title: this.getParameterValue('title'),
            boxWidth: this.getParameterValue('boxWidth') || '100%',
            boxPaddingLeft: this.getParameterValue('boxPaddingLeft') || '0',
            boxPaddingRight: this.getParameterValue('boxPaddingRight') || '20px'
        }
    }
}
