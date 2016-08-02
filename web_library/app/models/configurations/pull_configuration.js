var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './configuration', '../mixins/parameterizable', '../../js/helpers/mixin_helper'], function (require, exports, configuration_1, parameterizable_1, mixin_helper_1) {
    "use strict";
    var PullConfiguration = (function (_super) {
        __extends(PullConfiguration, _super);
        function PullConfiguration(id, mechanisms, active, parameters) {
            _super.call(this, id, mechanisms);
            this.dialogId = 'pullConfiguration';
            this.active = active;
            this.parameters = parameters;
        }
        PullConfiguration.initByData = function (data) {
            return new PullConfiguration(data.id, data.mechanisms, data.active, data.parameters);
        };
        PullConfiguration.prototype.shouldGetTriggered = function () {
            return this.getParameterValue('askOnAppStartup') || Math.random() <= this.getParameterValue('likelihood');
        };
        return PullConfiguration;
    }(configuration_1.Configuration));
    exports.PullConfiguration = PullConfiguration;
    mixin_helper_1.applyMixins(PullConfiguration, [parameterizable_1.Parameterizable]);
});
//# sourceMappingURL=pull_configuration.js.map