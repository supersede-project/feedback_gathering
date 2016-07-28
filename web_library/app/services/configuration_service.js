define(["require", "exports", './backends/http_backend', '../models/configuration'], function (require, exports, http_backend_1, configuration_1) {
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
        ConfigurationService.prototype.retrieveConfiguration = function (callback) {
            this.backend.retrieve(1, function (configuration) {
                var configurationObject = configuration_1.Configuration.initByData(configuration);
                callback(configurationObject);
            });
        };
        return ConfigurationService;
    }());
    exports.ConfigurationService = ConfigurationService;
});
//# sourceMappingURL=configuration_service.js.map