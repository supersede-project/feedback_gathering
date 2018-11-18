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
            let configurationObject = ConfigurationFactory.createByData(configuration);
            if(configurationObject !== null) {
                configurations.push(configurationObject);
            }
        }
        return new Application(data.id, data.name, data.state, generalConfiguration, configurations);
    }

    getPushConfiguration(): PushConfiguration {
        return <PushConfiguration>this.configurations.filter(configuration => configuration.type === configurationTypes.push)[0];
    }

    getElementSpecificPushConfigurations(): [PushConfiguration] {
        return <[PushConfiguration]>this.configurations.filter(configuration => configuration.type === configurationTypes.elementSpecificPush);
    }

    getPullConfigurations(): PullConfiguration[] {
        if(this.configurations) {
            return <PullConfiguration[]>this.configurations.filter(configuration => configuration.type === configurationTypes.pull);
        }
        return null;
    }

    /**
     * @returns any
     *  Context object
     */
    getContextForView() {
        return {
            buttonRowStyle: this.getGeneralConfigurationParameterValue('buttonRowStyle') || '',
            reviewButtonPosition: this.getGeneralConfigurationParameterValue('reviewButtonPosition'),
            reviewStyle: this.getReviewStyle(),
            mandatorySign: this.getGeneralConfigurationParameterValue('mandatorySign'),
            mandatoryLabelStyle: this.getMandatoryLabelStyle(),
            discardAsButton: this.getGeneralConfigurationParameterValue('discardAsButton'),
            discardClass: this.getGeneralConfigurationParameterValue('discardClass') || '',
            submissionPageMessage: this.getGeneralConfigurationParameterValue('submissionPageMessage'),
            labelPositioning: this.getGeneralConfigurationParameterValue('labelPositioning') === 'top' ? '' : 'horizontal',
            feedbackFormTitle: this.getGeneralConfigurationParameterValue('feedbackFormTitle'),
            generalLabelStyle: this.getGeneralLabelStyle(),
            dialogTitle: this.getGeneralConfigurationParameterValue('dialogTitle')
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
            if (this.getGeneralConfigurationParameterValue(parameterPropertyPair.parameter) !== null) {
                var unit = this.getCSSPropertyUnit(parameterPropertyPair.property);
                cssStyles += parameterPropertyPair.property + ': ' + this.getGeneralConfigurationParameterValue(parameterPropertyPair.parameter) + unit + ';';
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

    private getGeneralConfigurationParameterValue(key: string): string {
        if (!this.generalConfiguration) {
            return '';
        }
        return this.generalConfiguration.getParameterValue(key);
    }

    private getReviewStyle(): string {
        let reviewStyle = '';

        if(this.generalConfiguration) {
            if(this.getGeneralConfigurationParameterValue('reviewFontType') === 'bold') {
                var reviewFontTypeCSSPair = new ParameterValuePropertyPair('reviewFontType', 'font-weight');
            } else {
                var reviewFontTypeCSSPair = new ParameterValuePropertyPair('reviewFontType', 'font-style');
            }
            reviewStyle = this.getCssStyle([
                new ParameterValuePropertyPair('reviewFontFamily', 'font-family'),
                reviewFontTypeCSSPair
            ]);
        }

        return reviewStyle;
    }

    private getMandatoryLabelStyle(): string {
        let mandatoryLabelStyle = '';

        if(this.generalConfiguration) {
            if(this.getGeneralConfigurationParameterValue('mandatoryLabelStyle') === 'bold') {
                mandatoryLabelStyle = this.getCssStyle([
                    new ParameterValuePropertyPair('mandatoryLabelStyle', 'font-weight')
                ]);
            } else {
                mandatoryLabelStyle = this.getCssStyle([
                    new ParameterValuePropertyPair('mandatoryLabelStyle', 'font-style')
                ]);
            }
        }

        return mandatoryLabelStyle;
    }

    private getGeneralLabelStyle(): string {
        let generalLabelStyle = '';

        if(this.generalConfiguration) {
            if(this.getGeneralConfigurationParameterValue('labelFontColor')) {
                generalLabelStyle += ' ' + this.getCssStyle([
                    new ParameterValuePropertyPair('labelFontColor', 'color')
                ]);
            }
            if(this.getGeneralConfigurationParameterValue('labelFontSize')) {
                generalLabelStyle += ' ' + this.getCssStyle([
                    new ParameterValuePropertyPair('labelFontSize', 'font-size')
                ]);
            }
            if(this.getGeneralConfigurationParameterValue('labelFontWeight')) {
                generalLabelStyle += ' ' + this.getCssStyle([
                    new ParameterValuePropertyPair('labelFontWeight', 'font-weight')
                ]);
            }
        }

        return generalLabelStyle;
    }
}