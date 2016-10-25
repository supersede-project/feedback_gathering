define(["require", "exports", '../configurations/general_configuration', '../configurations/configuration_factory', '../../js/config', '../parameters/parameter_value_property_pair'], function (require, exports, general_configuration_1, configuration_factory_1, config_1, parameter_value_property_pair_1) {
    "use strict";
    var Application = (function () {
        function Application(id, name, state, generalConfiguration, configurations) {
            this.id = id;
            this.name = name;
            this.state = state;
            this.generalConfiguration = generalConfiguration;
            this.configurations = configurations;
        }
        Application.initByData = function (data) {
            var generalConfiguration = general_configuration_1.GeneralConfiguration.initByData(data.generalConfiguration);
            var configurations = [];
            for (var _i = 0, _a = data.configurations; _i < _a.length; _i++) {
                var configuration = _a[_i];
                configurations.push(configuration_factory_1.ConfigurationFactory.createByData(configuration));
            }
            return new Application(data.id, data.name, data.state, generalConfiguration, configurations);
        };
        Application.prototype.getPushConfiguration = function () {
            return this.configurations.filter(function (configuration) { return configuration.type === config_1.configurationTypes.push; })[0];
        };
        Application.prototype.getPullConfigurations = function () {
            return this.configurations.filter(function (configuration) { return configuration.type === config_1.configurationTypes.pull; });
        };
        Application.prototype.getContextForView = function () {
            if (this.generalConfiguration.getParameterValue('reviewFontType') === 'bold') {
                var reviewFontTypeCSSPair = new parameter_value_property_pair_1.ParameterValuePropertyPair('reviewFontType', 'font-weight');
            }
            else {
                var reviewFontTypeCSSPair = new parameter_value_property_pair_1.ParameterValuePropertyPair('reviewFontType', 'font-style');
            }
            var reviewStyle = this.getCssStyle([
                new parameter_value_property_pair_1.ParameterValuePropertyPair('reviewFontFamily', 'font-family'),
                reviewFontTypeCSSPair
            ]);
            if (this.generalConfiguration.getParameterValue('mandatoryLabelStyle') === 'bold') {
                var mandatoryLabelStyle = this.getCssStyle([
                    new parameter_value_property_pair_1.ParameterValuePropertyPair('mandatoryLabelStyle', 'font-weight'),
                ]);
            }
            else {
                var mandatoryLabelStyle = this.getCssStyle([
                    new parameter_value_property_pair_1.ParameterValuePropertyPair('mandatoryLabelStyle', 'font-style'),
                ]);
            }
            return {
                reviewButtonPosition: this.generalConfiguration.getParameterValue('reviewButtonPosition'),
                reviewStyle: reviewStyle,
                mandatorySign: this.generalConfiguration.getParameterValue('mandatorySign'),
                mandatoryLabelStyle: mandatoryLabelStyle,
                discardAsButton: this.generalConfiguration.getParameterValue('discardAsButton'),
                submissionPageMessage: this.generalConfiguration.getParameterValue('submissionPageMessage'),
            };
        };
        Application.prototype.getCssStyle = function (parameterValuePropertyPair) {
            var cssStyles = '';
            for (var i = 0; i < parameterValuePropertyPair.length; i++) {
                var parameterPropertyPair = parameterValuePropertyPair[i];
                if (this.generalConfiguration.getParameterValue(parameterPropertyPair.parameter) !== null) {
                    var unit = this.getCSSPropertyUnit(parameterPropertyPair.property);
                    cssStyles += parameterPropertyPair.property + ': ' + this.generalConfiguration.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
                    if (i !== parameterValuePropertyPair.length - 1) {
                        cssStyles += ' ';
                    }
                }
            }
            return cssStyles;
        };
        Application.prototype.getCSSPropertyUnit = function (property) {
            if (property === 'font-size') {
                return 'px';
            }
            else {
                return '';
            }
        };
        return Application;
    }());
    exports.Application = Application;
});
//# sourceMappingURL=application.js.map