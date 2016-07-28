define(["require", "exports"], function (require, exports) {
    "use strict";
    var PullConfiguration = (function () {
        function PullConfiguration(id, active, parameters, mechanism) {
            this.id = id;
            this.active = active;
            this.parameters = parameters;
            this.mechanism = mechanism;
        }
        return PullConfiguration;
    }());
    exports.PullConfiguration = PullConfiguration;
});
//# sourceMappingURL=pull_configuration.js.map