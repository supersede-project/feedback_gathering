define(["require", "exports"], function (require, exports) {
    "use strict";
    var MockBackend = (function () {
        function MockBackend(mockData) {
            this.mockData = mockData;
        }
        MockBackend.prototype.list = function (callback) {
            callback(this.mockData);
        };
        MockBackend.prototype.retrieve = function (id, callback) {
            callback(this.findById(id));
        };
        MockBackend.prototype.create = function (object, callback) {
            this.mockData.push(object);
            if (callback) {
                callback(object);
            }
        };
        MockBackend.prototype.update = function (id, attributes, callback) {
            var object = this.findById(id);
            for (var id_1 in attributes) {
                if (attributes.hasOwnProperty(id_1)) {
                    object[id_1] = attributes[id_1];
                }
            }
            if (callback) {
                callback(object);
            }
        };
        MockBackend.prototype.destroy = function (id, callback) {
            var object = this.findById(id);
            var index = this.mockData.indexOf(object);
            if (index !== undefined) {
                this.mockData.splice(index, 1);
            }
            if (callback) {
                callback(object);
            }
        };
        MockBackend.prototype.findById = function (id) {
            var resultArray = this.mockData.filter(function (obj) {
                return obj.id === id;
            });
            if (resultArray === null || resultArray.length === 0) {
                return null;
            }
            else {
                return resultArray[0];
            }
        };
        return MockBackend;
    }());
    exports.MockBackend = MockBackend;
});
//# sourceMappingURL=mock_backend.js.map