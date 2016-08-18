define(["require", "exports"], function (require, exports) {
    "use strict";
    var HttpBackend = (function () {
        function HttpBackend(path, apiEndpoint) {
            this.path = path;
            this.apiEndpoint = apiEndpoint;
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
            return this.apiEndpoint + this.path;
        };
        return HttpBackend;
    }());
    exports.HttpBackend = HttpBackend;
});
//# sourceMappingURL=http_backend.js.map