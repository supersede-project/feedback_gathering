import {ConfigurationInterface} from './configuration_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';
import {screenshotType, ratingType, textType} from '../../js/config';
import {MechanismFactory} from '../mechanisms/mechanism_factory';


export abstract class Configuration implements ConfigurationInterface {
    id:number;
    mechanisms:Mechanism[];
    dialogId:string;

    constructor(id:number, mechanisms:Mechanism[]) {
        this.id = id;
        this.mechanisms = mechanisms;
    }

    /**
     * @param mechanismTypeConstant
     *  The ID of the mechanism to get config of
     * @returns Mechanism
     *  The mechanism object with all its configuration data if the mechanism was found in the data, otherwise null.
     */
    getMechanismConfig(mechanismTypeConstant:string): any {
        var filteredArray = this.mechanisms.filter(mechanism => mechanism.type === mechanismTypeConstant);
        if(filteredArray.length > 0) {
            return MechanismFactory.createByData(filteredArray[0]);
        } else {
            return null;
        }
    }

    /**
     * @returns any
     *  Context object that contains all the data to configure the feedback mechanism in the view.
     */
    getContextForView() {
        var context = {textMechanism: null, ratingMechanism: null, screenshotMechanism: null, dialogId: this.dialogId};
        var textMechanism = this.getMechanismConfig(textType);
        var ratingMechanism = this.getMechanismConfig(ratingType);
        var screenshotMechanism = this.getMechanismConfig(screenshotType);

        var labelStyle = this.getCssStyle(textMechanism, [
            new ParameterValuePropertyPair('labelPositioning', 'text-align'),
            new ParameterValuePropertyPair('labelColor', 'color'),
            new ParameterValuePropertyPair('labelFontSize', 'font-size')]
        );
        var textareaStyle = this.getCssStyle(textMechanism, [new ParameterValuePropertyPair('textareaFontColor', 'color')]);

        if(textMechanism) {
            context.textMechanism = {
                active: textMechanism.active,
                hint: textMechanism.getParameterValue('hint'),
                label: textMechanism.getParameterValue('label'),
                currentLength: 0,
                maxLength: textMechanism.getParameterValue('maxLength'),
                maxLengthVisible: textMechanism.getParameterValue('maxLengthVisible'),
                textareaStyle: textareaStyle,
                labelStyle: labelStyle,
                clearInput: textMechanism.getParameterValue('clearInput'),
                mandatory: textMechanism.getParameterValue('mandatory'),
                mandatoryReminder: textMechanism.getParameterValue('mandatoryReminder'),
                validateOnSkip: textMechanism.getParameterValue('validateOnSkip')
            }
        }
        if(ratingMechanism) {
            context.ratingMechanism =  {
                active: ratingMechanism.active,
                title: ratingMechanism.getParameterValue('title')
            }
        }
        if(screenshotMechanism) {
            context.screenshotMechanism =  {
                active: screenshotMechanism.active,
            }
        }
        return context;
    }

    /**
     * Method to generate a html style value string.
     *
     * @param mechanism
     *  Mechanism to get the parameters from
     * @param parameterValuePropertyPair
     *  The pairs of 1) parameter in the parameters of the config and 2) the css property to use for this parameter's value
     * @returns {string}
     */
    getCssStyle(mechanism:Mechanism, parameterValuePropertyPair:ParameterValuePropertyPair[]): string {
        var cssStyles = '';
        for(var i = 0; i < parameterValuePropertyPair.length; i++) {
            var parameterPropertyPair = parameterValuePropertyPair[i];
            if (mechanism.getParameterValue(parameterPropertyPair.parameter) !== null) {
                var unit = this.getCSSPropertyUnit(parameterPropertyPair.property);
                cssStyles += parameterPropertyPair.property + ': ' + mechanism.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
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
}