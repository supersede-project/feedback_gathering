define(["require", "exports", '../models/parameter_property_pair', '../models/mechanism_factory', '../js/config'], function (require, exports, parameter_property_pair_1, mechanism_factory_1, config_1) {
    "use strict";
    var ConfigurationService = (function () {
        function ConfigurationService(data) {
            this.data = data;
        }
        ConfigurationService.prototype.getConfig = function () {
            var config = [];
            for (var i = 0; i < this.data.length; i++) {
                var element = this.data[i];
                if (element.hasOwnProperty('type')) {
                    var mechanism = this.getMechanismConfig(element['type']);
                    config.push(mechanism);
                }
            }
            return config;
        };
        ConfigurationService.prototype.getMechanismConfig = function (mechanismTypeConstant) {
            var filteredArray = this.data.filter(function (mechanism) { return mechanism.type === mechanismTypeConstant; });
            if (filteredArray.length > 0) {
                return mechanism_factory_1.MechanismFactory.createByData(filteredArray[0]);
            }
            else {
                return null;
            }
        };
        ConfigurationService.prototype.getContextForView = function () {
            var context = { textMechanism: null, ratingMechanism: null, screenshotMechanism: null };
            var textMechanism = this.getMechanismConfig(config_1.textType);
            var ratingMechanism = this.getMechanismConfig(config_1.ratingType);
            var screenshotMechanism = this.getMechanismConfig(config_1.screenshotType);
            var labelStyle = this.getCssStyle(textMechanism, [
                new parameter_property_pair_1.ParameterPropertyPair('labelPositioning', 'text-align'),
                new parameter_property_pair_1.ParameterPropertyPair('labelColor', 'color'),
                new parameter_property_pair_1.ParameterPropertyPair('labelFontSize', 'font-size')]);
            var textareaStyle = this.getCssStyle(textMechanism, [new parameter_property_pair_1.ParameterPropertyPair('textareaFontColor', 'color')]);
            if (textMechanism) {
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
                };
            }
            if (ratingMechanism) {
                context.ratingMechanism = {
                    active: ratingMechanism.active,
                    title: ratingMechanism.getParameterValue('title')
                };
            }
            if (screenshotMechanism) {
                context.screenshotMechanism = {
                    active: screenshotMechanism.active,
                };
            }
            return context;
        };
        ConfigurationService.prototype.getCssStyle = function (mechanism, parameterPropertyPairs) {
            var cssStyles = '';
            for (var i = 0; i < parameterPropertyPairs.length; i++) {
                var parameterPropertyPair = parameterPropertyPairs[i];
                if (mechanism.getParameterValue(parameterPropertyPair.parameter) !== null) {
                    var unit = ConfigurationService.getCSSPropertyUnit(parameterPropertyPair.property);
                    cssStyles += parameterPropertyPair.property + ': ' + mechanism.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
                    if (i !== parameterPropertyPairs.length - 1) {
                        cssStyles += ' ';
                    }
                }
            }
            return cssStyles;
        };
        ConfigurationService.getCSSPropertyUnit = function (property) {
            if (property === 'font-size') {
                return 'px';
            }
            else {
                return '';
            }
        };
        return ConfigurationService;
    }());
    exports.ConfigurationService = ConfigurationService;
});
//# sourceMappingURL=configuration_service.js.map