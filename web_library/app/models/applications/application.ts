import {GeneralConfiguration} from '../configurations/general_configuration';
import {ConfigurationInterface} from '../configurations/configuration_interface';
import {PullConfiguration} from '../configurations/pull_configuration';
import {PushConfiguration} from '../configurations/push_configuration';
import {ConfigurationFactory} from '../configurations/configuration_factory';
import {configurationTypes} from '../../js/config';
import {ParameterValuePropertyPair} from '../parameters/parameter_value_property_pair';


export class Application {
    id:number;
    name:string;
    state:number;
    generalConfiguration:GeneralConfiguration;
    configurations:ConfigurationInterface[];

    constructor(id:number, name:string, state:number, generalConfiguration:GeneralConfiguration, configurations:ConfigurationInterface[]) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.generalConfiguration = generalConfiguration;
        this.configurations = configurations;
    }

    static initByData(data:any) {
        var generalConfiguration = GeneralConfiguration.initByData(data.generalConfiguration);
        var configurations = [];
        for(var configuration of data.configurations) {
            configurations.push(ConfigurationFactory.createByData(configuration));
        }
        return new Application(data.id, data.name, data.state, generalConfiguration, configurations);
    }

    getPushConfiguration(): PushConfiguration {
        return <PushConfiguration>this.configurations.filter(configuration => configuration.type === configurationTypes.push)[0];
    }

    getPullConfigurations(): PullConfiguration[] {
        return <PullConfiguration[]>this.configurations.filter(configuration => configuration.type === configurationTypes.pull);
    }

    /**
     * @returns any
     *  Context object
     */
    getContextForView() {
        if(this.generalConfiguration.getParameterValue('reviewFontType') === 'bold') {
            var reviewFontTypeCSSPair = new ParameterValuePropertyPair('reviewFontType', 'font-weight');
        } else {
            var reviewFontTypeCSSPair = new ParameterValuePropertyPair('reviewFontType', 'font-style');
        }

        var reviewStyle = this.getCssStyle([
            new ParameterValuePropertyPair('reviewFontFamily', 'font-family'),
            reviewFontTypeCSSPair
        ]);

        if(this.generalConfiguration.getParameterValue('mandatoryLabelStyle') === 'bold') {
            var mandatoryLabelStyle = this.getCssStyle([
                new ParameterValuePropertyPair('mandatoryLabelStyle', 'font-weight'),
            ]);
        } else {
            var mandatoryLabelStyle = this.getCssStyle([
                new ParameterValuePropertyPair('mandatoryLabelStyle', 'font-style'),
            ]);
        }

        var generalLabelStyle = '';
        if(this.generalConfiguration.getParameterValue('labelFontColor')) {
            generalLabelStyle += ' ' + this.getCssStyle([
                    new ParameterValuePropertyPair('labelFontColor', 'color'),
                ]);
        }
        if(this.generalConfiguration.getParameterValue('labelFontSize')) {
            generalLabelStyle += ' ' + this.getCssStyle([
                    new ParameterValuePropertyPair('labelFontSize', 'font-size'),
                ]);
        }
        if(this.generalConfiguration.getParameterValue('labelFontWeight')) {
            generalLabelStyle += ' ' + this.getCssStyle([
                    new ParameterValuePropertyPair('labelFontWeight', 'font-weight'),
                ]);
        }

        return {
            reviewButtonPosition: this.generalConfiguration.getParameterValue('reviewButtonPosition'),
            reviewStyle: reviewStyle,
            mandatorySign: this.generalConfiguration.getParameterValue('mandatorySign'),
            mandatoryLabelStyle: mandatoryLabelStyle,
            discardAsButton: this.generalConfiguration.getParameterValue('discardAsButton'),
            discardPositionClass: this.generalConfiguration.getParameterValue('discardPositionClass') || '',
            submissionPageMessage: this.generalConfiguration.getParameterValue('submissionPageMessage'),
            labelPositioning: this.generalConfiguration.getParameterValue('labelPositioning') === 'top' ? '' : 'horizontal',
            feedbackFormTitle: this.generalConfiguration.getParameterValue('feedbackFormTitle'),
            generalLabelStyle: generalLabelStyle,
        };
    }

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
            if (this.generalConfiguration.getParameterValue(parameterPropertyPair.parameter) !== null) {
                var unit = this.getCSSPropertyUnit(parameterPropertyPair.property);
                cssStyles += parameterPropertyPair.property + ': ' + this.generalConfiguration.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
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