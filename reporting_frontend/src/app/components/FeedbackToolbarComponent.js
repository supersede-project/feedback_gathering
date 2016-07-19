"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
/**
 * Created by flo on 15.07.16.
 */
var core_1 = require('@angular/core');
var button_1 = require('@angular2-material/button');
var input_1 = require('@angular2-material/input/input');
var FeedbackToolbarComponent = (function () {
    function FeedbackToolbarComponent(feedbackService) {
        this.feedbackService = feedbackService;
    }
    FeedbackToolbarComponent.prototype.getFeedbacks = function (appname) {
        this.feedbackService.GetFeedbacks(appname);
    };
    FeedbackToolbarComponent = __decorate([
        core_1.Component({
            selector: 'feedback-toolbar',
            template: "\n  <div style=\"float: left\">\n    <md-input placeholder=\"enter application name\" #appname maxlength=\"100\" class=\"demo-full-width\">\n    </md-input>\n  </div>\n  <button style=\"margin-left: 10px\" md-raised-button (click) = \"getFeedbacks(appname.value)\">load</button>\n",
            directives: [button_1.MdButton, input_1.MD_INPUT_DIRECTIVES]
        })
    ], FeedbackToolbarComponent);
    return FeedbackToolbarComponent;
}());
exports.FeedbackToolbarComponent = FeedbackToolbarComponent;
//# sourceMappingURL=FeedbackToolbarComponent.js.map