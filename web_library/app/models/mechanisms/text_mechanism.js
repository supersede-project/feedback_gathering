var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './mechanism', '../parameters/parameter_value_property_pair'], function (require, exports, mechanism_1, parameter_value_property_pair_1) {
    "use strict";
    var TextMechanism = (function (_super) {
        __extends(TextMechanism, _super);
        function TextMechanism(id, type, active, order, canBeActivated, parameters) {
            _super.call(this, id, type, active, order, canBeActivated, parameters);
        }
        TextMechanism.prototype.getContext = function () {
            var textareaStyle = this.getCssStyle([new parameter_value_property_pair_1.ParameterValuePropertyPair('textareaFontColor', 'color')]);
            var labelStyle = this.getCssStyle([
                new parameter_value_property_pair_1.ParameterValuePropertyPair('labelPositioning', 'text-align'),
                new parameter_value_property_pair_1.ParameterValuePropertyPair('labelColor', 'color'),
                new parameter_value_property_pair_1.ParameterValuePropertyPair('labelFontSize', 'font-size')]);
            return {
                active: this.active,
                hint: this.getParameterValue('hint'),
                label: this.getParameterValue('label'),
                currentLength: 0,
                maxLength: this.getParameterValue('maxLength'),
                maxLengthVisible: this.getParameterValue('maxLengthVisible'),
                textareaStyle: textareaStyle,
                labelStyle: labelStyle,
                clearInput: this.getParameterValue('clearInput'),
                mandatory: this.getParameterValue('mandatory'),
                mandatoryReminder: this.getParameterValue('mandatoryReminder'),
                validateOnSkip: this.getParameterValue('validateOnSkip')
            };
        };
        return TextMechanism;
    }(mechanism_1.Mechanism));
    exports.TextMechanism = TextMechanism;
});
//# sourceMappingURL=text_mechanism.js.map