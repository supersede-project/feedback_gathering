define(["require", "exports", '../../js/config', './push_configuration', '../mechanisms/mechanism_factory', './general_configuration', './pull_configuration'], function (require, exports, config_1, push_configuration_1, mechanism_factory_1, general_configuration_1, pull_configuration_1) {
    "use strict";
    var ConfigurationFactory = (function () {
        function ConfigurationFactory() {
        }
        ConfigurationFactory.createByData = function (data) {
            if (!data.hasOwnProperty('type')) {
                return null;
            }
            var generalConfiguration = general_configuration_1.GeneralConfiguration.initByData(data.generalConfiguration);
            var mechanisms = [];
            for (var _i = 0, _a = data.mechanisms; _i < _a.length; _i++) {
                var mechanism = _a[_i];
                var mechanismObject = mechanism_factory_1.MechanismFactory.createByData(mechanism);
                if (mechanismObject !== null) {
                    mechanisms.push(mechanismObject);
                }
            }
            if (data.type === config_1.configurationTypes.push) {
                return new push_configuration_1.PushConfiguration(data.id, mechanisms, generalConfiguration);
            }
            else if (data.type === config_1.configurationTypes.pull) {
                return new pull_configuration_1.PullConfiguration(data.id, mechanisms, generalConfiguration);
            }
            else {
                return null;
            }
        };
        return ConfigurationFactory;
    }());
    exports.ConfigurationFactory = ConfigurationFactory;
});
//# sourceMappingURL=configuration_factory.js.map