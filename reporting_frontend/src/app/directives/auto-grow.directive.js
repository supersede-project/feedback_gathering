"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
/**
 * Created by flo on 14.07.16.
 */
var core_1 = require('@angular/core');
var AutoGrowDirective = (function () {
    function AutoGrowDirective(el, renderer) {
        this.el = el;
        this.renderer = renderer;
    }
    AutoGrowDirective.prototype.onFocus = function () {
        this.renderer.setElementStyle(this.el, 'width', '200');
    };
    AutoGrowDirective.prototype.onBlur = function () {
        this.renderer.setElementStyle(this.el, 'width', '120');
    };
    AutoGrowDirective = __decorate([
        core_1.Directive({
            selector: '[autoGrow]',
            host: {
                '(focus)': 'onFocus()',
                '(blur)': 'onBlur()'
            }
        })
    ], AutoGrowDirective);
    return AutoGrowDirective;
}());
exports.AutoGrowDirective = AutoGrowDirective;
//# sourceMappingURL=auto-grow.directive.js.map