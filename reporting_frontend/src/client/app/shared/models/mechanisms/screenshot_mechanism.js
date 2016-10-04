var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './mechanism'], function (require, exports, mechanism_1) {
    "use strict";
    var ScreenshotMechanism = (function (_super) {
        __extends(ScreenshotMechanism, _super);
        function ScreenshotMechanism(id, type, active, order, canBeActivated, parameters, screenshotView) {
            _super.call(this, id, type, active, order, canBeActivated, parameters);
            this.screenshotView = screenshotView;
        }
        ScreenshotMechanism.prototype.setScreenshotView = function (screenshotView) {
            this.screenshotView = screenshotView;
        };
        ScreenshotMechanism.prototype.getContext = function () {
            return {
                autoTake: this.getParameterValue('autoTake')
            };
        };
        return ScreenshotMechanism;
    }(mechanism_1.Mechanism));
    exports.ScreenshotMechanism = ScreenshotMechanism;
});
//# sourceMappingURL=screenshot_mechanism.js.map