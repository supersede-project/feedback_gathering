import {Category} from './category';


export class CategoryFeedback {
    mechanismId:number;
    categories:Category[];

    constructor(mechanismId:number, categories:Category[]) {
        this.mechanismId = mechanismId;
        this.categories = categories;
    }
}

