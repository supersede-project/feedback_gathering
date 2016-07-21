/**
 * Created by flo on 15.07.16.
 */
import {Component} from '@angular/core'
import {Feedback} from '../models/Feedback'
import {FeedbackService} from '../services/feedback.service'
import { MD_BUTTON_DIRECTIVES } from '@angular2-material/button';
import {MD_INPUT_DIRECTIVES} from '@angular2-material/input'
import {MD_TOOLBAR_DIRECTIVES} from '@angular2-material/toolbar'

@Component({
  selector: 'feedback-toolbar',
  template:`
  <md-toolbar color="primary">
     Reporting
     <md-input placeholder="enter application name" #appname maxlength="100" style="position:absolute; top:5px; right:70px"></md-input>
     <button md-fab style="position:absolute; top:5px; right:10px" (click) = "getFeedbacks(appname.value)">load</button>
  </md-toolbar>
`,
  directives: [MD_BUTTON_DIRECTIVES, MD_INPUT_DIRECTIVES, MD_TOOLBAR_DIRECTIVES]

})
export class FeedbackToolbarComponent{
  constructor(private feedbackService: FeedbackService){
  }

  private getFeedbacks(appname: string){
      this.feedbackService.GetFeedbacks(appname);
  }
}
