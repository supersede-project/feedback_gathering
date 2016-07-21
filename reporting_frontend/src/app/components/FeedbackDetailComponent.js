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
var Feedback_1 = require('../models/Feedback');
var card_1 = require('@angular2-material/card');
var FeedbackDetailComponent = (function () {
    function FeedbackDetailComponent(feedbackService) {
        var _this = this;
        this.feedbackService = feedbackService;
        this.selectedFeedback = new Feedback_1.Feedback();
        this.detailShowing = false;
        this.feedbackService.selectedFeedbackEvent.subscribe(function (data) {
            _this.selectedFeedback = data;
            _this.detailShowing = true;
        });
    }
    FeedbackDetailComponent = __decorate([
        core_1.Component({
            selector: 'feedback-detail',
            template: "\n<div *ngIf=\"detailShowing\">\n  <md-card>\n    <md-card-subtitle>from user \"{{selectedFeedback.user}}\"</md-card-subtitle>\n    <md-card-title>{{selectedFeedback.title}}</md-card-title>\n    <md-card-content>\n      <h3 md-subheader>Text</h3>\n      <p>{{selectedFeedback.text}}</p>\n      <h3 md-subheader>Ratings</h3>\n      <h3 md-subheader>Screenshots</h3>\n        <div id=\"screenshot_container\" style=\"overflow-x: auto; margin-top: 10px; max-height:500px; max-width: 100%\">\n          <div id=\"screenshot_wrapper\" style=\"width:10000px; max-height: 500px\">\n            <img class=\"screenshot\" style=\"width: auto; max-height: 500px; margin-left: 10px\" src=\"https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg\"/>\n            <img class=\"screenshot\" style=\"width: auto; max-height: 500px; margin-left: 10px\" src=\"https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg\"/>\n            <img class=\"screenshot\" style=\"width: auto; max-height: 500px; margin-left: 10px\" src=\"https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg\"/>\n            <img class=\"screenshot\" style=\"width: auto; max-height: 500px; margin-left: 10px\" src=\"https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg\"/>\n            <img class=\"screenshot\" style=\"width: auto; max-height: 500px; margin-left: 10px\" src=\"https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg\"/>\n          </div>\n      </div>\n    </md-card-content>\n    <md-card-actions align=\"end\">\n      <button md-raised-button>reply</button>\n    </md-card-actions>\n  </md-card>\n</div>\n",
            directives: [card_1.MD_CARD_DIRECTIVES]
        })
    ], FeedbackDetailComponent);
    return FeedbackDetailComponent;
}());
exports.FeedbackDetailComponent = FeedbackDetailComponent;
//# sourceMappingURL=FeedbackDetailComponent.js.map