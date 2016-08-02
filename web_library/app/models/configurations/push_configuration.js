var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './configuration'], function (require, exports, configuration_1) {
    "use strict";
    var PushConfiguration = (function (_super) {
        __extends(PushConfiguration, _super);
        function PushConfiguration(id, mechanisms, general_configurations, pull_configurations) {
            _super.call(this, id, mechanisms);
            this.dialogId = 'pushConfiguration';
            this.general_configurations = general_configurations;
            this.pull_configurations = pull_configurations;
        }
        PushConfiguration.initByData = function (data) {
            return new PushConfiguration(data.id, data.mechanisms, data.general_configurations, data.pull_configurations);
        };
        return PushConfiguration;
    }(configuration_1.Configuration));
    exports.PushConfiguration = PushConfiguration;
});
//# sourceMappingURL=push_configuration.js.map