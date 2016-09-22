define(["require", "exports"], function (require, exports) {
    "use strict";
    var CanvasState = (function () {
        function CanvasState(src, width, height) {
            this.src = src;
            this.width = width;
            this.height = height;
        }
        return CanvasState;
    }());
    exports.CanvasState = CanvasState;
});
//# sourceMappingURL=canvas_state.js.map