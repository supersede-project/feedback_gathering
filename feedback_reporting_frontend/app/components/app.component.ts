import {Component} from 'angular2/core';
import {FeedbacksComponent} from "./FeedbacksComponent";

@Component({
    selector: 'my-app',
    template: '<h1>Feedback Reporting</h1><feedbacks></feedbacks>',
    directives: [FeedbacksComponent]
})
export class AppComponent { }