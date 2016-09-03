var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './mechanism'], function (require, exports, mechanism_1) {
    "use strict";
    var AttachmentMechanism = (function (_super) {
        __extends(AttachmentMechanism, _super);
        function AttachmentMechanism(id, type, active, order, canBeActivated, parameters) {
            _super.call(this, id, type, active, order, canBeActivated, parameters);
        }
        AttachmentMechanism.prototype.getContext = function () {
            return {
                title: this.getParameterValue('title')
            };
        };
        return AttachmentMechanism;
    }(mechanism_1.Mechanism));
    exports.AttachmentMechanism = AttachmentMechanism;
});
//# sourceMappingURL=attachment_mechanism.js.map