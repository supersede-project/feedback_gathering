import {ConfigurationInterface} from './configuration_interface';
import {Mechanism} from '../mechanisms/mechanism';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';
import {GeneralConfiguration} from './general_configuration';
import {mechanismTypes, configurationTypes} from '../../js/config';
import {PushConfiguration} from './push_configuration';
import {PullConfiguration} from './pull_configuration';
import {CategoryMechanism} from '../mechanisms/category_mechanism';


export abstract class Configuration implements ConfigurationInterface {
    id:number;
    mechanisms:Mechanism[];
    type:string;
    generalConfiguration:GeneralConfiguration;
    dialogId:string;

    constructor(id:number, mechanisms:Mechanism[], type:string, generalConfiguration:GeneralConfiguration) {
        this.id = id;
        this.mechanisms = mechanisms;
        this.type = type;
        this.generalConfiguration = generalConfiguration;
    }

    /**
     * @param mechanismTypeConstant
     *  The ID of the mechanism to get config of
     * @returns Mechanism
     *  The mechanism object with all its configuration data if the mechanism was found in the data, otherwise null.
     */
    getMechanismConfig(mechanismTypeConstant:string): Mechanism {
        var filteredArray = this.mechanisms.filter(mechanism => mechanism.type === mechanismTypeConstant);
        if(filteredArray.length > 0) {
            return filteredArray[0];
        } else {
            return null;
        }
    }

    /**
     * @returns any
     *  Context object that contains all the data to configure the feedback mechanism in the view.
     */
    getContextForView() {
        var context = {
            textMechanism: null,
            ratingMechanism: null,
            screenshotMechanism: null,
            categoryMechanism: null,
            dialogId: this.dialogId
        };
        var textMechanism = this.getMechanismConfig(mechanismTypes.textType);
        var ratingMechanism = this.getMechanismConfig(mechanismTypes.ratingType);
        var screenshotMechanism = this.getMechanismConfig(mechanismTypes.screenshotType);
        var categoryMechanism:CategoryMechanism = <CategoryMechanism>this.getMechanismConfig(mechanismTypes.categoryType);

        // TODO move this to the mechanism classes
        if(textMechanism) {
            var textareaStyle = this.getCssStyle(textMechanism, [new ParameterValuePropertyPair('textareaFontColor', 'color')]);
            var labelStyle = this.getCssStyle(textMechanism, [
                new ParameterValuePropertyPair('labelPositioning', 'text-align'),
                new ParameterValuePropertyPair('labelColor', 'color'),
                new ParameterValuePropertyPair('labelFontSize', 'font-size')]
            );
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
                autoTake: screenshotMechanism.getParameterValue('autoTake')
            }
        }
        if(categoryMechanism) {
            context.categoryMechanism = {
                id: categoryMechanism.id,
                active: categoryMechanism.active,
                title: categoryMechanism.getParameterValue('title'),
                ownAllowed: categoryMechanism.getParameterValue('ownAllowed'),
                multiple: categoryMechanism.getParameterValue('multiple'),
                breakAfterOption: categoryMechanism.getParameterValue('breakAfterOption') ? true : false,
                options: categoryMechanism.getOptions(),
                inputType: categoryMechanism.getParameterValue('multiple') ? 'checkbox' : 'radio'
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