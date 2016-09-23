define(["require", "exports"], function (require, exports) {
    "use strict";
    var CanvasState = (function () {
        function CanvasState(src, width, height, shiftTop, shiftLeft) {
            this.src = src;
            this.width = width;
            this.height = height;
            this.shiftTop = shiftTop;
            this.shiftLeft = shiftLeft;
        }
        return CanvasState;
    }());
    exports.CanvasState = CanvasState;
});
//# sourceMappingURL=canvas_state.js.map