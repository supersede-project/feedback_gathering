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
 * Created by flo on 14.07.16.
 */
var core_1 = require('@angular/core');
var feedback_service_1 = require('../services/feedback.service');
var button_1 = require('@angular2-material/button');
var list_1 = require('@angular2-material/list');
var input_1 = require('@angular2-material/input');
var FeedbacksComponent = (function () {
    function FeedbacksComponent(feedbackService) {
        var _this = this;
        this.feedbackService = feedbackService;
        this.feedbackService.feedbackListEvent.subscribe(function (data) { return _this.feedbacks = data; });
    }
    FeedbacksComponent.prototype.showFeedbackDetail = function (feedback) {
        this.feedbackService.SelectFeedback(feedback);
    };
    FeedbacksComponent = __decorate([
        core_1.Component({
            selector: "feedback-list",
            template: "\n  <md-nav-list>\n     <md-list-item *ngFor=\"let feedback of feedbacks\" (click)=\"showFeedbackDetail(feedback)\">\n        <h3 style=\"font-weight: bold\" md-line>{{feedback.title}}</h3>\n        <p md-line style=\"color: #90a4ae\">{{feedback.created}}</p>\n     </md-list-item>\n  </md-nav-list>\n",
            directives: [button_1.MdButton, input_1.MD_INPUT_DIRECTIVES, list_1.MD_LIST_DIRECTIVES]
        }), 
        __metadata('design:paramtypes', [feedback_service_1.FeedbackService])
    ], FeedbacksComponent);
    return FeedbacksComponent;
}());
exports.FeedbacksComponent = FeedbacksComponent;
//# sourceMappingURL=FeedbacksComponent.js.map