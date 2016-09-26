import {CategoryType} from './category_type';


export class Category {
    mechanismId:number;
    text:string;
    categoryType:CategoryType;

    constructor(mechanismId:number, text:string, categoryTypeId:number, categoryType:CategoryType) {
        this.mechanismId = mechanismId;
        this.text = text;
        this.categoryType = categoryType;
    }
}

