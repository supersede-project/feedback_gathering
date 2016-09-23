define(["require", "exports"], function (require, exports) {
    "use strict";
    var HttpBackend = (function () {
        function HttpBackend(path, apiEndpoint, language) {
            this.path = path;
            this.apiEndpoint = apiEndpoint;
            this.language = language;
        }
        HttpBackend.prototype.list = function (callback) {
            var url = this.getUrl();
            jQuery.ajax({
                url: url,
                dataType: 'json',
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
            var url = this.getUrl() + id;
            jQuery.ajax({
                url: url,
                dataType: 'json',
                type: 'GET',
                success: function (data) {
                    callback(data);
                },
                error: function (data) {
                    callback(data);
                }
            });
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
            return (this.apiEndpoint + this.path).replace('{lang}', this.language);
        };
        return HttpBackend;
    }());
    exports.HttpBackend = HttpBackend;
});
//# sourceMappingURL=http_backend.js.map