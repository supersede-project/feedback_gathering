define(["require", "exports", '../parameters/parameter_value_property_pair', '../../js/config'], function (require, exports, parameter_value_property_pair_1, config_1) {
    "use strict";
    var Configuration = (function () {
        function Configuration(id, mechanisms, type, generalConfiguration) {
            this.id = id;
            this.mechanisms = mechanisms;
            this.type = type;
            this.generalConfiguration = generalConfiguration;
        }
        Configuration.prototype.getMechanismConfig = function (mechanismTypeConstant) {
            var filteredArray = this.mechanisms.filter(function (mechanism) { return mechanism.type === mechanismTypeConstant; });
            if (filteredArray.length > 0) {
                return filteredArray[0];
            }
            else {
                return null;
            }
        };
        Configuration.prototype.getContextForView = function () {
            var context = {
                textMechanism: null,
                ratingMechanism: null,
                screenshotMechanism: null,
                categoryMechanism: null,
                dialogId: this.dialogId
            };
            var textMechanism = this.getMechanismConfig(config_1.mechanismTypes.textType);
            var ratingMechanism = this.getMechanismConfig(config_1.mechanismTypes.ratingType);
            var screenshotMechanism = this.getMechanismConfig(config_1.mechanismTypes.screenshotType);
            var categoryMechanism = this.getMechanismConfig(config_1.mechanismTypes.categoryType);
            if (textMechanism) {
                var textareaStyle = this.getCssStyle(textMechanism, [new parameter_value_property_pair_1.ParameterValuePropertyPair('textareaFontColor', 'color')]);
                var labelStyle = this.getCssStyle(textMechanism, [
                    new parameter_value_property_pair_1.ParameterValuePropertyPair('labelPositioning', 'text-align'),
                    new parameter_value_property_pair_1.ParameterValuePropertyPair('labelColor', 'color'),
                    new parameter_value_property_pair_1.ParameterValuePropertyPair('labelFontSize', 'font-size')]);
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
            if (categoryMechanism) {
                context.categoryMechanism = {
                    id: categoryMechanism.id,
                    active: categoryMechanism.active,
                    title: categoryMechanism.getParameterValue('title'),
                    ownAllowed: categoryMechanism.getParameterValue('ownAllowed'),
                    multiple: categoryMechanism.getParameterValue('multiple'),
                    breakAfterOption: categoryMechanism.getParameterValue('breakAfterOption') ? true : false,
                    options: categoryMechanism.getOptions(),
                    inputType: categoryMechanism.getParameterValue('multiple') ? 'checkbox' : 'radio'
                };
            }
            return context;
        };
        Configuration.prototype.getCssStyle = function (mechanism, parameterValuePropertyPair) {
            var cssStyles = '';
            for (var i = 0; i < parameterValuePropertyPair.length; i++) {
                var parameterPropertyPair = parameterValuePropertyPair[i];
                if (mechanism.getParameterValue(parameterPropertyPair.parameter) !== null) {
                    var unit = this.getCSSPropertyUnit(parameterPropertyPair.property);
                    cssStyles += parameterPropertyPair.property + ': ' + mechanism.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
                    if (i !== parameterValuePropertyPair.length - 1) {
                        cssStyles += ' ';
                    }
                }
            }
            return cssStyles;
        };
        Configuration.prototype.getCSSPropertyUnit = function (property) {
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