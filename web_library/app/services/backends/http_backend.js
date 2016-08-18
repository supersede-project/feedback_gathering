define(["require", "exports", '../../js/config'], function (require, exports, config_1) {
    "use strict";
    var HttpBackend = (function () {
        function HttpBackend(path) {
            this.path = path;
        }
        HttpBackend.prototype.list = function (callback) {
            var url = this.getUrl();
            jQuery.ajax({
                url: url,
                type: 'GET',
                success: function (data) {
                    callback(data);
                },
                error: function (data) {
                    callback(data);
                }
            });
        };
        HttpBackend.prototype.retrieve = function (id, callback) {
            return null;
        };
        HttpBackend.prototype.create = function (object, callback) {
            return null;
        };
        HttpBackend.prototype.update = function (id, attributes, callback) {
            return null;
        };
        HttpBackend.prototype.destroy = function (id, callback) {
            return null;
        };
        HttpBackend.prototype.getUrl = function () {
            return config_1.apiEndpoint + this.path;
        };
        return HttpBackend;
    }());
    exports.HttpBackend = HttpBackend;
});
//# sourceMappingURL=http_backend.js.map