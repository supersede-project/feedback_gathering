define(["require", "exports", '../configurations/general_configuration', '../configurations/configuration_factory', '../../js/config'], function (require, exports, general_configuration_1, configuration_factory_1, config_1) {
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
            return {
                reviewButtonPosition: this.generalConfiguration.getParameterValue('reviewButtonPosition')
            };
        };
        return Application;
    }());
    exports.Application = Application;
});
//# sourceMappingURL=application.js.map