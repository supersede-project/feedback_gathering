"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
/**
 * Created by flo on 15.07.16.
 */
var core_1 = require('@angular/core');
var feedback_service_1 = require('../services/feedback.service');
var button_1 = require('@angular2-material/button');
var input_1 = require('@angular2-material/input');
var toolbar_1 = require('@angular2-material/toolbar');
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
            template: "\n  <md-toolbar color=\"primary\">\n     Reporting\n     <md-input placeholder=\"enter application name\" #appname maxlength=\"100\" style=\"position:absolute; top:5px; right:70px\"></md-input>\n     <button md-fab style=\"position:absolute; top:5px; right:10px\" (click) = \"getFeedbacks(appname.value)\">load</button>\n  </md-toolbar>\n",
            directives: [button_1.MD_BUTTON_DIRECTIVES, input_1.MD_INPUT_DIRECTIVES, toolbar_1.MD_TOOLBAR_DIRECTIVES]
        }), 
        __metadata('design:paramtypes', [feedback_service_1.FeedbackService])
    ], FeedbackToolbarComponent);
    return FeedbackToolbarComponent;
}());
exports.FeedbackToolbarComponent = FeedbackToolbarComponent;
//# sourceMappingURL=FeedbackToolbarComponent.js.map