/**
 * Created by flo on 15.07.16.
 */
import {Component, OnInit} from '@angular/core'
import {Feedback} from '../models/Feedback'
import {FeedbackService} from '../services/feedback.service'
import {MD_CARD_DIRECTIVES} from '@angular2-material/card'

@Component({
  selector: 'feedback-detail',
  template: `
<div>
  <md-card>
     {{selectedFeedback.title}}
  </md-card>
</div>
`,
  directives: [MD_CARD_DIRECTIVES]
})
export class FeedbackDetailComponent {

  private selectedFeedback: Feedback;

  constructor(private feedbackService: FeedbackService){
    this.selectedFeedback = new Feedback();
    this.feedbackService.selectedFeedbackEvent.subscribe(data => this.selectedFeedback = data)
  }
}
