import {ParameterInterface} from '../parameters/parameter_interface';
import {Parameter} from '../parameters/parameter';


export class Mechanism {
    id:number;
    type:string;
    active:boolean;
    order: number;
    canBeActivated:boolean;
    parameters: ParameterInterface[];

    constructor(id: number, type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:ParameterInterface[]) {
        this.id = id;
        this.type = type;
        this.active = active;
        this.order = order;
        this.canBeActivated = canBeActivated;
        this.parameters = parameters;
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
