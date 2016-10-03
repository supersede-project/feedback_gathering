define(["require", "exports", '../mixins/parameterizable', '../../js/helpers/mixin_helper'], function (require, exports, parameterizable_1, mixin_helper_1) {
    "use strict";
    var Mechanism = (function () {
        function Mechanism(id, type, active, order, canBeActivated, parameters) {
            this.id = id;
            this.type = type;
            this.active = active;
            this.order = order;
            this.canBeActivated = canBeActivated;
            this.parameters = parameters;
        }
        Mechanism.prototype.getContext = function () { };
        Mechanism.prototype.getCssStyle = function (parameterValuePropertyPair) {
            var cssStyles = '';
            for (var i = 0; i < parameterValuePropertyPair.length; i++) {
                var parameterPropertyPair = parameterValuePropertyPair[i];
                if (this.getParameterValue(parameterPropertyPair.parameter) !== null) {
                    var unit = this.getCSSPropertyUnit(parameterPropertyPair.property);
                    cssStyles += parameterPropertyPair.property + ': ' + this.getParameterValue(parameterPropertyPair.parameter) + unit + ';';
                    if (i !== parameterValuePropertyPair.length - 1) {
                        cssStyles += ' ';
                    }
                }
            }
            return cssStyles;
        };
        Mechanism.prototype.getCSSPropertyUnit = function (property) {
            if (property === 'font-size') {
                return 'px';
            }
            else {
                return '';
            }
        };
        return Mechanism;
    }());
    exports.Mechanism = Mechanism;
    mixin_helper_1.applyMixins(Mechanism, [parameterizable_1.Parameterizable]);
});
//# sourceMappingURL=mechanism.js.map