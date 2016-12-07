import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';


export class AudioMechanism extends Mechanism {

    constructor(id:number, type:string, active:boolean, order:number, canBeActivated:boolean, parameters:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }

    getContext():any {
        return {
            maxTime: this.getParameterValue('maxTime'),
            title: this.getParameterValue('title'),
            boxWidth: this.getParameterValue('boxWidth'),
            boxPaddingLeft: this.getParameterValue('boxPaddingLeft') || '0',
            boxPaddingRight: this.getParameterValue('boxPaddingRight') || '20px'
        }
    }
}
