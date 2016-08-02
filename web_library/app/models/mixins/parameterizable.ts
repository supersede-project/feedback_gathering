import {ParameterInterface} from '../parameters/parameter_interface';


/**
 * Class to be used as a mixin in classes that contain an array of parameters. Usage as follows:
 *
 * class Example implements Parameterizable {
 *      parameters:ParameterInterface[];
 *
 *      ...
 *
 *      getParameter: (key:string) => ParameterInterface;
 *      getParameterValue: (key:string) => any;
 * }
 *
 * applyMixins(Example, [Parameterizable]);
 *
 * This makes the implementation available in the Example class, therefore it provides a kind of mixin.
 */
export class Parameterizable {
    parameters: ParameterInterface[];

    /**
     * @param key
     *  The key of the key value pair of a parameter object
     * @returns {any}
     *  The parameter object, containing the value and further data
     */
    getParameter(key:string): ParameterInterface {
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
