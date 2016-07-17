System.register(['angular2/core', '../services/feedback.service'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, feedback_service_1;
    var FeedbacksComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (feedback_service_1_1) {
                feedback_service_1 = feedback_service_1_1;
            }],
        execute: function() {
            FeedbacksComponent = (function () {
                function FeedbacksComponent(feedbackService) {
                    this.feedbackService = feedbackService;
                }
                FeedbacksComponent.prototype.getFeedbacks = function (appname) {
                    var _this = this;
                    this.application = appname;
                    if (this.application != "") {
                        this.feedbackService
                            .GetFeedbacks(this.application)
                            .subscribe(function (data) { return _this.feedbacks = data; }, function (error) { return console.log(error); });
                    }
                };
                FeedbacksComponent = __decorate([
                    core_1.Component({
                        selector: "feedbacks",
                        template: "\n<h2>Feedbacks</h2>\n<p>Enter an application: <input #appname type=\"text\"> <button md-button (click) = \"getFeedbacks(appname.value)\">load</button></p>\n<ul>\n    <li *ngFor=\"#feedback of feedbacks\">\n        Title: {{feedback.title}}\n        Text: {{feedback.text}}\n    </li>\n</ul>\n",
                        providers: [feedback_service_1.FeedbackService]
                    }), 
                    __metadata('design:paramtypes', [feedback_service_1.FeedbackService])
                ], FeedbacksComponent);
                return FeedbacksComponent;
            }());
            exports_1("FeedbacksComponent", FeedbacksComponent);
        }
    }
});
//# sourceMappingURL=FeedbacksComponent.js.map