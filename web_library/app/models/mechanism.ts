import {Parameter} from './parameter';


export const textType = 'TEXT_TYPE';
export const ratingType = 'RATING_TYPE';


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

    /**
     * @param data
     * @returns Mechanism
     *  The mechanism object parsed from json.
     */
    static initByData(data:any): Mechanism {
        if(!data.hasOwnProperty('type') || !data.hasOwnProperty('active')) {
            return null;
        }
        return new Mechanism(data.type, data.active, data.order, data.canBeActivated, data.parameters);
    }

    /**
     * @param key
     *  The key of the key value pair of a parameter object
     * @returns {any}
     *  The parameter object, containing the value and further data
     */
    getParameter(key:string): Parameter {
        var filteredArray = this.parameters.filter(parameter => parameter.key === key);
        if(filteredArray.length > 0) {
            return filteredArray[0];
        } else {
            return null;
        }
    }

    /**
     * @param key
     *  The key of the key value pair of a parameter object
     * @returns any
     *  The parameter value or null
     */
    getParameterValue(key:string): any {
        var parameter = this.getParameter(key);
        if(parameter == null || !parameter.hasOwnProperty('value')) {
            return null;
        } else {
            return parameter.value
        }
    }
}
