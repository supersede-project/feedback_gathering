import {Mechanism} from './mechanism';
import {Parameter} from '../parameters/parameter';
import {ParameterInterface} from '../parameters/parameter_interface';


export class RatingMechanism extends Mechanism {
    currentRatingValue:number;
    initialRating:number;

    constructor(id: number, type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:ParameterInterface[]) {
        super(id, type, active, order, canBeActivated, parameters);
        this.initialRating = this.getParameterValue('defaultRating');
        this.currentRatingValue = this.getParameterValue('defaultRating');
    }
}
