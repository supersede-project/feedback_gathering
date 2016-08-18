define(["require", "exports", './backends/http_backend', '../models/applications/application'], function (require, exports, http_backend_1, application_1) {
    "use strict";
    var ApplicationService = (function () {
        function ApplicationService(backend) {
            if (!backend) {
                this.backend = new http_backend_1.HttpBackend('feedback_orchestrator/example/configuration');
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