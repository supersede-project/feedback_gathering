var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './mechanism'], function (require, exports, mechanism_1) {
    "use strict";
    var AudioMechanism = (function (_super) {
        __extends(AudioMechanism, _super);
        function AudioMechanism(id, type, active, order, canBeActivated, parameters) {
            _super.call(this, id, type, active, order, canBeActivated, parameters);
        }
        AudioMechanism.prototype.getContext = function () {
            return {
                maxTime: this.getParameterValue('maxTime'),
                title: this.getParameterValue('title')
            };
        };
        return AudioMechanism;
    }(mechanism_1.Mechanism));
    exports.AudioMechanism = AudioMechanism;
});
//# sourceMappingURL=audio_mechanism.js.map