define(["require", "exports", '../js/config'], function (require, exports, config_1) {
    "use strict";
    var HttpBackend = (function () {
        function HttpBackend(path) {
            this.path = path;
        }
        HttpBackend.prototype.list = function () {
            return null;
        };
        HttpBackend.prototype.retrieve = function (id) {
            return null;
        };
        HttpBackend.prototype.create = function (object) {
            return null;
        };
        HttpBackend.prototype.update = function (id, attributes) {
            return null;
        };
        HttpBackend.prototype.destroy = function (id) {
            return null;
        };
        HttpBackend.prototype.getUrl = function () {
            return config_1.apiEndpoint + config_1.orchestratorBasePath + this.path;
        };
        return HttpBackend;
    }());
    exports.HttpBackend = HttpBackend;
});
//# sourceMappingURL=http_backend.js.map