export class RatingFeedback {
    title:string;
    rating:number;
    mechanismId:number;

    constructor(title:string, rating:number, mechanismId:number) {
        this.title = title;
        this.rating = rating;
        this.mechanismId = mechanismId;
    }
}

