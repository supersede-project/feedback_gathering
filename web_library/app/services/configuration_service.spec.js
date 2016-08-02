define(["require", "exports", './configuration_service', './backends/mock_backend', './mocks/mocks_loader'], function (require, exports, configuration_service_1, mock_backend_1, mocks_loader_1) {
    "use strict";
    describe('PushConfiguration Service', function () {
        var configurationService;
        beforeEach(function () {
            var configurationMockData = mocks_loader_1.readJSON('app/services/mocks/configurations_mock.json');
            var backend = new mock_backend_1.MockBackend(configurationMockData);
            configurationService = new configuration_service_1.ConfigurationService(backend);
        });
        it('should have the correct backend object', function () {
        });
        it('should retrieve the configuration from a backend', function () {
        });
    });
});
//# sourceMappingURL=configuration_service.spec.js.map