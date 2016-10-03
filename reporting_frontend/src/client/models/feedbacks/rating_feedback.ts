import {RatingMechanism} from '../mechanisms/rating_mechanism';


export class RatingFeedback {
    rating:number;
    mechanismId:number;
    mechanism:RatingMechanism;

    constructor(rating:number, mechanismId:number) {
        this.rating = rating;
        this.mechanismId = mechanismId;
    }
}

