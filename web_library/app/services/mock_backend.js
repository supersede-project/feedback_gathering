define(["require", "exports"], function (require, exports) {
    "use strict";
    var MockBackend = (function () {
        function MockBackend(mockData) {
            this.mockData = mockData;
        }
        MockBackend.prototype.list = function () {
            return this.mockData;
        };
        MockBackend.prototype.retrieve = function (id) {
            return this.findById(id);
        };
        MockBackend.prototype.create = function (object) {
            this.mockData.push(object);
            return object;
        };
        MockBackend.prototype.update = function (id, attributes) {
            var object = this.findById(id);
            for (var id_1 in attributes) {
                if (attributes.hasOwnProperty(id_1)) {
                    object[id_1] = attributes[id_1];
                }
            }
            return object;
        };
        MockBackend.prototype.destroy = function (id) {
            var object = this.findById(id);
            var index = this.mockData.indexOf(object);
            if (index !== undefined) {
                this.mockData.splice(index, 1);
            }
            return object;
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