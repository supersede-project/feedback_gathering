define(["require", "exports"], function (require, exports) {
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
                return filteredArray;
            }
            else {
                return [];
            }
        };
        Configuration.prototype.getMechanismsSorted = function () {
            return this.mechanisms.sort(function (a, b) {
                return (a.order > b.order) ? 1 : ((b.order > a.order) ? -1 : 0);
            });
        };
        Configuration.prototype.getContextForView = function () {
            var context = {
                dialogId: this.dialogId,
                mechanisms: []
            };
            for (var _i = 0, _a = this.getMechanismsSorted(); _i < _a.length; _i++) {
                var mechanism = _a[_i];
                var mechanismContext = jQuery.extend({}, mechanism, mechanism.getContext());
                context.mechanisms.push(mechanismContext);
            }
            return context;
        };
        return Configuration;
    }());
    exports.Configuration = Configuration;
});
//# sourceMappingURL=configuration.js.map