import {Parameter} from './parameter';
import {Mechanism} from './mechanism';


export class RatingMechanism extends Mechanism {
    currentRatingValue:number;
    initialRating:number;

    constructor(type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:Parameter[]) {
        super(type, active, order, canBeActivated, parameters);
        this.initialRating = this.getParameterValue('defaultRating');
        this.currentRatingValue = this.getParameterValue('defaultRating');
    }

    getRatingElementOptions(): {} {
        var ratingMechanismObject = this;

        return {
            starSize: 25,
            totalStars: this.getParameterValue('maxRating'),
            initialRating: this.initialRating,
            useFullStars: true,
            disableAfterRate: false,
            callback: function (currentRating, $el) {
                ratingMechanismObject.currentRatingValue = currentRating;
            }
        };
    }
}
