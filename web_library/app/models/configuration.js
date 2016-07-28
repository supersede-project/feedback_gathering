define(["require", "exports", './mechanism_factory', './parameter_property_pair', '../js/config'], function (require, exports, mechanism_factory_1, parameter_property_pair_1, config_1) {
    "use strict";
    var Configuration = (function () {
        function Configuration(id, mechanisms, general_configurations, pull_configurations) {
            this.id = id;
            this.general_configurations = general_configurations;
            this.pull_configurations = pull_configurations;
            this.mechanisms = mechanisms;
        }
        Configuration.initByData = function (data) {
            return new Configuration(data.id, data.mechanisms, data.general_configurations, data.pull_configurations);
        };
        Configuration.prototype.getMechanismConfig = function (mechanismTypeConstant) {
            var filteredArray = this.mechanisms.filter(function (mechanism) { return mechanism.type === mechanismTypeConstant; });
            if (filteredArray.length > 0) {
                return mechanism_factory_1.MechanismFactory.createByData(filteredArray[0]);
            }
            else {
                return null;
            }
        };
        Configuration.prototype.getContextForView = function () {
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
        Configuration.prototype.getCssStyle = function (mechanism, parameterPropertyPairs) {
            var cssStyles = '';
            for (var i = 0; i < parameterPropertyPairs.length; i++) {
                var parameterPropertyPair = parameterPropertyPairs[i];
                if (mechanism.getParameterValue(parameterPropertyPair.parameter) !== null) {
                    var unit = Configuration.getCSSPropertyUnit(parameterPropertyPair.property);
                    cssStyles += parameterPropertyPair.property + ': ' + mechanism.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
                    if (i !== parameterPropertyPairs.length - 1) {
                        cssStyles += ' ';
                    }
                }
            }
            return cssStyles;
        };
        Configuration.getCSSPropertyUnit = function (property) {
            if (property === 'font-size') {
                return 'px';
            }
            else {
                return '';
            }
        };
        return Configuration;
    }());
    exports.Configuration = Configuration;
});
//# sourceMappingURL=configuration.js.map