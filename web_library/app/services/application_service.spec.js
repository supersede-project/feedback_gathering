define(["require", "exports", './backends/mock_backend', './mocks/mocks_loader', './application_service'], function (require, exports, mock_backend_1, mocks_loader_1, application_service_1) {
    "use strict";
    describe('Application Service', function () {
        var applicationService;
        beforeEach(function () {
            var applicationMockData = mocks_loader_1.readJSON('app/services/mocks/applications_mock.json', '/base/');
            var backend = new mock_backend_1.MockBackend(applicationMockData);
            applicationService = new application_service_1.ApplicationService(backend);
        });
        it('should retrieve the application from a backend', function () {
            applicationService.retrieveApplication(1, function (application) {
                expect(application.name).toEqual('Senercon Website');
                expect(application.configurations.length).toBe(3);
            });
        });
    });
});
//# sourceMappingURL=application_service.spec.js.map