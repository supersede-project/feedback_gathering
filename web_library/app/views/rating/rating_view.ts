import {RatingMechanism} from '../../models/mechanisms/rating_mechanism';
import {RatingFeedback} from '../../models/feedbacks/rating_feedback';
import {MechanismView} from '../mechanism_view';


export class RatingView implements MechanismView {

    constructor(private ratingMechanism:RatingMechanism, private dialogId:string) {
        if (ratingMechanism.active) {
            var options = ratingMechanism.getRatingElementOptions();
            jQuery('' + this.getSelector()).starRating(options);
        }
    }

    getSelector():string {
        return "#" + this.dialogId + " #ratingMechanism" + this.ratingMechanism.id + " .rating-input";
    }

    reset() {
        if(jQuery('' + this.getSelector()).starRating) {
            jQuery('' + this.getSelector()).starRating('setRating', this.ratingMechanism.getParameterValue('defaultRating'));
        }
        this.ratingMechanism.currentRatingValue = this.ratingMechanism.getParameterValue('defaultRating');

    }

    getFeedback():RatingFeedback {
        return new RatingFeedback(this.ratingMechanism.currentRatingValue, this.ratingMechanism.id, this.ratingMechanism.getParameterValue('title'));
    }
}