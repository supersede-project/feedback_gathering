import {ParameterInterface} from '../parameters/parameter_interface';
import {Parameterizable} from '../mixins/parameterizable';
import {applyMixins} from '../../js/helpers/mixin_helper';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';


/**
 * Base class for the mechanisms. Note that this class is extended by the Parameterizable mixin to provide methods on a
 * parameter array field.
 */
export abstract class Mechanism implements Parameterizable {
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

    getParameter: (key:string) => ParameterInterface;
    getParameterValue: (key:string) => any;
    getContext(): any {}


    /**
     * Method to generate a html style value string.
     *
     * @param parameterValuePropertyPair
     *  The pairs of 1) parameter in the parameters of the config and 2) the css property to use for this parameter's value
     * @returns {string}
     */
    getCssStyle(parameterValuePropertyPair:ParameterValuePropertyPair[]): string {
        var cssStyles = '';
        for(var i = 0; i < parameterValuePropertyPair.length; i++) {
            var parameterPropertyPair = parameterValuePropertyPair[i];
            if (this.getParameterValue(parameterPropertyPair.parameter) !== null) {
                var unit = this.getCSSPropertyUnit(parameterPropertyPair.property);
                cssStyles += parameterPropertyPair.property + ': ' + this.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
                if(i !== parameterValuePropertyPair.length - 1) {
                    cssStyles += ' ';
                }
            }
        }
        return cssStyles;
    }

    getCSSPropertyUnit(property: string) {
        if(property === 'font-size') {
            return 'px'
        } else {
            return '';
        }
    }

    defaultContext() {
        return {
            'id': this.id,
            'type': this.type,
            'active': this.active,
            'order': this.order,
            'canBeActivated': this.canBeActivated
        }
    }
}

applyMixins(Mechanism, [Parameterizable]);