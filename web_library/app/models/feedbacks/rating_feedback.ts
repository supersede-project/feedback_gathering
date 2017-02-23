export class RatingFeedback {
    rating:number;
    mechanismId:number;
    title:string;

    constructor(rating:number, mechanismId:number, title:string) {
        this.rating = rating;
        this.mechanismId = mechanismId;
        this.title = title;
    }
}

