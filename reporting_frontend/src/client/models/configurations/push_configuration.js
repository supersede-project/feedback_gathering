var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './configuration', '../../js/config'], function (require, exports, configuration_1, config_1) {
    "use strict";
    var PushConfiguration = (function (_super) {
        __extends(PushConfiguration, _super);
        function PushConfiguration(id, mechanisms, generalConfiguration) {
            _super.call(this, id, mechanisms, config_1.configurationTypes.push, generalConfiguration);
            this.dialogId = 'pushConfiguration';
        }
        return PushConfiguration;
    }(configuration_1.Configuration));
    exports.PushConfiguration = PushConfiguration;
});
//# sourceMappingURL=push_configuration.js.map