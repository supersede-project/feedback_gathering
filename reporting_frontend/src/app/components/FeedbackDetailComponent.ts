/**
 * Created by flo on 15.07.16.
 */
import {Component, OnInit} from '@angular/core'
import {Feedback} from '../models/Feedback'
import {FeedbackService} from '../services/feedback.service'
import {MD_CARD_DIRECTIVES} from '@angular2-material/card'
import {MD_BUTTON_DIRECTIVES} from '@angular2-material/button'

@Component({
  selector: 'feedback-detail',
  template: `
<div *ngIf="detailShowing">
  <md-card>
    <md-card-subtitle>from user "{{selectedFeedback.user}}"</md-card-subtitle>
    <md-card-title>{{selectedFeedback.title}}</md-card-title>
    <md-card-content>
      <h3 md-subheader>Text</h3>
      <p>{{selectedFeedback.text}}</p>
      <h3 md-subheader>Ratings</h3>
      <h3 md-subheader>Screenshots</h3>
        <div id="screenshot_container" style="overflow-x: auto; margin-top: 10px; max-height:500px; max-width: 100%">
          <div id="screenshot_wrapper" style="width:10000px; max-height: 500px">
            <img class="screenshot" style="width: auto; max-height: 500px; margin-left: 10px" src="https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg"/>
            <img class="screenshot" style="width: auto; max-height: 500px; margin-left: 10px" src="https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg"/>
            <img class="screenshot" style="width: auto; max-height: 500px; margin-left: 10px" src="https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg"/>
            <img class="screenshot" style="width: auto; max-height: 500px; margin-left: 10px" src="https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg"/>
            <img class="screenshot" style="width: auto; max-height: 500px; margin-left: 10px" src="https://s-media-cache-ak0.pinimg.com/564x/ab/0e/ce/ab0ece23e44a78bb48df99973c1c4f82.jpg"/>
          </div>
      </div>
    </md-card-content>
    <md-card-actions align="end">
      <button md-raised-button>reply</button>
    </md-card-actions>
  </md-card>
</div>
`,
  directives: [MD_CARD_DIRECTIVES]
})
export class FeedbackDetailComponent {

  private selectedFeedback: Feedback;
  private detailShowing: boolean;

  constructor(private feedbackService: FeedbackService){
    this.selectedFeedback = new Feedback();
    this.detailShowing = false;

    this.feedbackService.selectedFeedbackEvent.subscribe(data => {
      this.selectedFeedback = data;
      this.detailShowing = true;
    })
  }
}
