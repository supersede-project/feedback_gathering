define(["require", "exports", '../models/mechanism', '../models/parameter_property_pair'], function (require, exports, mechanism_1, parameter_property_pair_1) {
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
                return mechanism_1.Mechanism.initByData(filteredArray[0]);
            }
            else {
                return null;
            }
        };
        ConfigurationService.prototype.getContextForView = function () {
            var context = { textMechanism: null, ratingMechanism: null };
            var textMechanism = this.getMechanismConfig(mechanism_1.textType);
            var ratingMechanism = this.getMechanismConfig(mechanism_1.ratingType);
            var labelStyle = this.getCssStyle(textMechanism, [
                new parameter_property_pair_1.ParameterPropertyPair('labelPositioning', 'text-align'),
                new parameter_property_pair_1.ParameterPropertyPair('labelColor', 'color'),
                new parameter_property_pair_1.ParameterPropertyPair('labelFontSize', 'font-size')]);
            var textareaStyle = this.getCssStyle(textMechanism, [new parameter_property_pair_1.ParameterPropertyPair('textareaFontColor', 'color')]);
            if (textMechanism) {
                context.textMechanism = {
                    active: textMechanism.active,
                    hint: textMechanism.getParameterValue('hint'),
                    currentLength: 0,
                    maxLength: textMechanism.getParameterValue('maxLength'),
                    maxLengthVisible: textMechanism.getParameterValue('maxLengthVisible'),
                    textareaStyle: textareaStyle,
                    labelStyle: labelStyle,
                };
            }
            if (ratingMechanism) {
                context.ratingMechanism = {
                    active: ratingMechanism.active,
                    title: ratingMechanism.getParameterValue('title')
                };
            }
            return context;
        };
        ConfigurationService.prototype.getCssStyle = function (mechanism, parameterPropertyPairs) {
            var cssStyles = '';
            for (var i = 0; i < parameterPropertyPairs.length; i++) {
                var parameterPropertyPair = parameterPropertyPairs[i];
                if (mechanism.getParameterValue(parameterPropertyPair.parameter) !== null) {
                    var unit = '';
                    if (parameterPropertyPair.property == 'font-size') {
                        unit = 'px';
                    }
                    cssStyles += parameterPropertyPair.property + ': ' + mechanism.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
                    if (i !== parameterPropertyPairs.length - 1) {
                        cssStyles += ' ';
                    }
                }
            }
            return cssStyles;
        };
        return ConfigurationService;
    }());
    exports.ConfigurationService = ConfigurationService;
});
