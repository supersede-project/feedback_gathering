define(["require", "exports", './backends/http_backend', '../models/applications/application', '../js/config'], function (require, exports, http_backend_1, application_1, config_1) {
    "use strict";
    var ApplicationService = (function () {
        function ApplicationService(language, backend) {
            if (!backend) {
                this.backend = new http_backend_1.HttpBackend(config_1.applicationPath, config_1.apiEndpointOrchestrator, language);
            }
            else {
                this.backend = backend;
            }
        }
        ApplicationService.prototype.retrieveApplication = function (applicationId, callback) {
            this.backend.retrieve(applicationId, function (applicationData) {
                var application = application_1.Application.initByData(applicationData);
                callback(application);
            });
        };
        return ApplicationService;
    }());
    exports.ApplicationService = ApplicationService;
});
//# sourceMappingURL=application_service.js.map