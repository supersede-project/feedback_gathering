import {Component} from '@angular/core';
import {FeedbacksComponent} from "./components/FeedbacksComponent";
import {FeedbackDetailComponent} from "./components/FeedbackDetailComponent";
import {FeedbackToolbarComponent} from "./components/FeedbackToolbarComponent";
import {FeedbackService} from "./services/feedback.service"
import {MD_LIST_DIRECTIVES} from "@angular2-material/list/list"

@Component({
    selector: 'my-app',
    template: `

<div id="main">
    <div id="toolbar">
      <feedback-toolbar></feedback-toolbar>
    </div>
    <div id="list">
      <feedback-list></feedback-list>
    </div>
    <div id="detail">
      <feedback-detail></feedback-detail>
    </div>
</div>
`,
    directives: [FeedbacksComponent, FeedbackDetailComponent, FeedbackToolbarComponent, MD_LIST_DIRECTIVES],
    styleUrls: ['app/app.component.css'],
    providers: [FeedbackService]
})
export class AppComponent { }
