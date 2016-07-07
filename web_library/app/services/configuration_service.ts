import {Mechanism} from '../models/mechanism';
import {ParameterPropertyPair} from '../models/parameter_property_pair';
import {MechanismFactory} from '../models/mechanism_factory';
import {textType, ratingType, screenshotType} from '../js/config';


/**
 * Handles the configuration data retrieved from the orchestrator core.
 */
export class ConfigurationService {
    private data:any;

    /**
     * @param data
     *  The json data coming from the orchestrator core
     */
    constructor(data: any) {
        this.data = data;
    }

    getConfig(): any[] {
        var config = [];
        for(var i = 0; i < this.data.length; i++) {
            var element = this.data[i];
            if(element.hasOwnProperty('type')) {
                var mechanism = this.getMechanismConfig(element['type']);
                config.push(mechanism);
            }
        }
        return config;
    }

    /**
     * @param mechanismTypeConstant
     *  The ID of the mechanism to get config of
     * @returns Mechanism
     *  The mechanism object with all its configuration data if the mechanism was found in the data, otherwise null.
     */
    getMechanismConfig(mechanismTypeConstant:string): any {
        var filteredArray = this.data.filter(mechanism => mechanism.type === mechanismTypeConstant);
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
        var context = {textMechanism: null, ratingMechanism: null, screenshotMechanism: null};
        var textMechanism = this.getMechanismConfig(textType);
        var ratingMechanism = this.getMechanismConfig(ratingType);
        var screenshotMechanism = this.getMechanismConfig(screenshotType);

        var labelStyle = this.getCssStyle(textMechanism, [
            new ParameterPropertyPair('labelPositioning', 'text-align'),
            new ParameterPropertyPair('labelColor', 'color'),
            new ParameterPropertyPair('labelFontSize', 'font-size')]
        );
        var textareaStyle = this.getCssStyle(textMechanism, [new ParameterPropertyPair('textareaFontColor', 'color')]);

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
     * @param parameterPropertyPairs
     *  The pairs of 1) parameter in the parameters of the config and 2) the css property to use for this parameter's value
     * @returns {string}
     */
    getCssStyle(mechanism:Mechanism, parameterPropertyPairs:ParameterPropertyPair[]): string {
        var cssStyles = '';
        for(var i = 0; i < parameterPropertyPairs.length; i++) {
            var parameterPropertyPair = parameterPropertyPairs[i];
            if (mechanism.getParameterValue(parameterPropertyPair.parameter) !== null) {
                var unit = ConfigurationService.getCSSPropertyUnit(parameterPropertyPair.property);
                cssStyles += parameterPropertyPair.property + ': ' + mechanism.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
                if(i !== parameterPropertyPairs.length - 1) {
                    cssStyles += ' ';
                }
            }
        }
        return cssStyles;
    }

    private static getCSSPropertyUnit(property: string) {
        if(property === 'font-size') {
            return 'px'
        } else {
            return '';
        }
    }
}
