import {Category} from './category';
import {CategoryMechanism} from '../mechanisms/category_mechanism';


export class CategoryFeedback {
    mechanismId:number;
    mechanism:CategoryMechanism;
    categories:Category[];
    parameterId:number;

    constructor(mechanismId:number, categories:Category[]) {
        this.mechanismId = mechanismId;
        this.categories = categories;
    }
}

