import {ParameterInterface} from '../parameters/parameter_interface';
import {Parameterizable} from '../mixins/parameterizable';
import {applyMixins} from '../../js/helpers/mixin_helper';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';


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
     * @param mechanism
     *  Mechanism to get the parameters from
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
        if(property === 'font-size' || property === 'height' || property === 'width' || property === 'border-width' ||
            property === 'margin-top' || property === 'margin-bottom') {
            return 'px'
        } else {
            return '';
        }
    }

    defaultContext() {
        let mechanismStyle = this.getCssStyle([
            new ParameterValuePropertyPair('marginTop', 'margin-top'),
            new ParameterValuePropertyPair('marginBottom', 'margin-bottom'),
        ]);

        return {
            'id': this.id,
            'type': this.type,
            'active': this.active,
            'order': this.order,
            'canBeActivated': this.canBeActivated,
            'page': this.getParameterValue('page') || 1,
            'cssClass': this.getParameterValue('cssClass') || '',
            'mechanismStyle': mechanismStyle
        }
    }
}

applyMixins(Mechanism, [Parameterizable]);