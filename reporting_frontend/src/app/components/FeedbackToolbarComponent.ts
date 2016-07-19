/**
 * Created by flo on 15.07.16.
 */
import {Component} from '@angular/core'
import {Feedback} from '../models/Feedback'
import {FeedbackService} from '../services/feedback.service'
import { MdButton } from '@angular2-material/button';
import {MD_INPUT_DIRECTIVES} from '@angular2-material/input/input'

@Component({
  selector: 'feedback-toolbar',
  template:`
  <div style="float: left">
    <md-input placeholder="enter application name" #appname maxlength="100" class="demo-full-width">
    </md-input>
  </div>
  <button style="margin-left: 10px" md-raised-button (click) = "getFeedbacks(appname.value)">load</button>
`,
  directives: [MdButton, MD_INPUT_DIRECTIVES]

})
export class FeedbackToolbarComponent{
  constructor(private feedbackService: FeedbackService){
  }

  private getFeedbacks(appname: string){
      this.feedbackService.GetFeedbacks(appname);
  }
}
