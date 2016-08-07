var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './configuration', '../../js/config'], function (require, exports, configuration_1, config_1) {
    "use strict";
    var PullConfiguration = (function (_super) {
        __extends(PullConfiguration, _super);
        function PullConfiguration(id, mechanisms, generalConfiguration) {
            _super.call(this, id, mechanisms, config_1.configurationTypes.pull, generalConfiguration);
            this.dialogId = 'pullConfiguration';
        }
        PullConfiguration.prototype.shouldGetTriggered = function () {
            return this.generalConfiguration.getParameterValue('askOnAppStartup') ||
                Math.random() <= this.generalConfiguration.getParameterValue('likelihood');
        };
        return PullConfiguration;
    }(configuration_1.Configuration));
    exports.PullConfiguration = PullConfiguration;
});
//# sourceMappingURL=pull_configuration.js.map