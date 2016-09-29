define(["require", "exports", '../mixins/parameterizable', '../../js/helpers/mixin_helper', '../parameters/parameter'], function (require, exports, parameterizable_1, mixin_helper_1, parameter_1) {
    "use strict";
    var GeneralConfiguration = (function () {
        function GeneralConfiguration(id, name, parameters) {
            this.id = id;
            this.name = name;
            this.parameters = parameters;
        }
        GeneralConfiguration.initByData = function (data) {
            if (!data) {
                return null;
            }
            var parameters = [];
            for (var _i = 0, _a = data.parameters; _i < _a.length; _i++) {
                var parameter = _a[_i];
                parameters.push(parameter_1.Parameter.initByData(parameter));
            }
            return new GeneralConfiguration(data.id, data.name, parameters);
        };
        return GeneralConfiguration;
    }());
    exports.GeneralConfiguration = GeneralConfiguration;
    mixin_helper_1.applyMixins(GeneralConfiguration, [parameterizable_1.Parameterizable]);
});
//# sourceMappingURL=general_configuration.js.map