import {Parameter} from './parameter';


export const textType = 'TEXT_TYPE';
export const ratingType = 'RATING_TYPE';
export const screenShotType = 'SCREEN_SHOT_TYPE';


export class Mechanism {
    type:string;
    active:boolean;
    order: number;
    canBeActivated:boolean;
    parameters: Parameter[];

    constructor(type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:Parameter[]) {
        this.type = type;
        this.active = active;
        this.order = order;
        this.canBeActivated = canBeActivated;
        this.parameters = parameters;
    }

    static initByData(data:any): Mechanism {
        if(data.type === null || data.active === null) {
            return null;
        }
        return new Mechanism(data.type, data.active, data.order, data.canBeActivated, data.parameters);
    }

    getParameter(key:string): Parameter {
        var filteredArray = this.parameters.filter(parameter => parameter.key === key);
        if(filteredArray.length > 0) {
            return filteredArray[0];
        } else {
            return null;
        }
    }
}
