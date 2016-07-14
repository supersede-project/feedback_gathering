/**
 * Created by flo on 14.07.16.
 */
import {Component} from 'angular2/core'
import {Feedback} from '../models/Feedback'
import {FeedbackService} from '../services/feedback.service'
import { MD_BUTTON_DIRECTIVES } from '@angular2-material/button'

@Component({
    selector: "feedbacks",
    template: `
<h2>Feedbacks</h2>
<p>Enter an application: <input #appname type="text"> <button md-button (click) = "getFeedbacks(appname.value)">load</button></p>
<ul>
    <li *ngFor="#feedback of feedbacks">
        Title: {{feedback.title}}
        Text: {{feedback.text}}
    </li>
</ul>
`,
    providers: [FeedbackService]
})
export class FeedbacksComponent{

    private feedbacks: Feedback[];
    private application: string;

    constructor(private feedbackService: FeedbackService){
    }

    private getFeedbacks(appname: string){
        this.application = appname;
        if (this.application != ""){
            this.feedbackService
                .GetFeedbacks(this.application)
                .subscribe((data: Feedback[]) => this.feedbacks = data, error => console.log(error));
        }
    }

}