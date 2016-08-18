define(["require", "exports", './backends/http_backend', '../models/configurations/configuration_factory'], function (require, exports, http_backend_1, configuration_factory_1) {
    "use strict";
    var ConfigurationService = (function () {
        function ConfigurationService(backend) {
            if (!backend) {
                this.backend = new http_backend_1.HttpBackend('feedback_orchestrator/example/configuration');
            }
            else {
                this.backend = backend;
            }
        }
        ConfigurationService.prototype.retrieveConfiguration = function (configurationId, callback) {
            this.backend.retrieve(configurationId, function (configurationData) {
                var configurationObject = configuration_factory_1.ConfigurationFactory.createByData(configurationData);
                callback(configurationObject);
            });
        };
        return ConfigurationService;
    }());
    exports.ConfigurationService = ConfigurationService;
});
//# sourceMappingURL=configuration_service.js.map