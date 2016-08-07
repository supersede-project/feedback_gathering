define(["require", "exports", './configuration_service', './backends/mock_backend', './mocks/mocks_loader'], function (require, exports, configuration_service_1, mock_backend_1, mocks_loader_1) {
    "use strict";
    describe('Configuration Service', function () {
        var configurationService;
        beforeEach(function () {
            var configurationMockData = mocks_loader_1.readJSON('app/services/mocks/configurations_mock.json', '/base/');
            var backend = new mock_backend_1.MockBackend(configurationMockData);
            configurationService = new configuration_service_1.ConfigurationService(backend);
        });
        it('should retrieve the configuration from a backend', function () {
            configurationService.retrieveConfiguration(1, function (configuration) {
                expect(configuration.type).toEqual('PUSH');
            });
        });
    });
});
//# sourceMappingURL=configuration_service.spec.js.map