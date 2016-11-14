import {RatingMechanism} from '../../models/mechanisms/rating_mechanism';


export class RatingView {

    constructor(private ratingMechanism:RatingMechanism, private dialogId:string) {
        if (ratingMechanism.active) {
            var options = ratingMechanism.getRatingElementOptions();
            jQuery('' + this.getSelector()).starRating(options);
            this.reset();
        }
    }

    getSelector():string {
        return "#" + this.dialogId + " #ratingMechanism" + this.ratingMechanism.id + " .rating-input";
    }

    reset() {
        if (this.ratingMechanism.initialRating) {
            jQuery('' + this.getSelector() + ' .jq-star:nth-child(' + this.ratingMechanism.initialRating + ')').click();
        }
    }
}