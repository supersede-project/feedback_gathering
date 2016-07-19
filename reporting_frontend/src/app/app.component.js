"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var FeedbacksComponent_1 = require("./components/FeedbacksComponent");
var FeedbackDetailComponent_1 = require("./components/FeedbackDetailComponent");
var FeedbackToolbarComponent_1 = require("./components/FeedbackToolbarComponent");
var feedback_service_1 = require("./services/feedback.service");
var list_1 = require("@angular2-material/list/list");
var AppComponent = (function () {
    function AppComponent() {
    }
    AppComponent = __decorate([
        core_1.Component({
            selector: 'my-app',
            template: "\n\n<div id=\"main\">\n    <div id=\"header\">\n    <h2 style=\"color: lightslategrey\">Feedback Reporting</h2>\n    </div>\n    <div id=\"feedback-toolbar\"><feedback-toolbar></feedback-toolbar></div>\n    <div id=\"feedback-list\"><feedback-list></feedback-list></div>\n    <div id=\"feedback-detail\"><feedback-detail></feedback-detail></div>\n</div>\n",
            directives: [FeedbacksComponent_1.FeedbacksComponent, FeedbackDetailComponent_1.FeedbackDetailComponent, FeedbackToolbarComponent_1.FeedbackToolbarComponent, list_1.MD_LIST_DIRECTIVES],
            styleUrls: ['app/app.component.css'],
            providers: [feedback_service_1.FeedbackService]
        })
    ], AppComponent);
    return AppComponent;
}());
exports.AppComponent = AppComponent;
//# sourceMappingURL=app.component.js.map