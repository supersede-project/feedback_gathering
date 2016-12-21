import {Pipe} from '@angular/core'
import {Feedback} from '../models/feedbacks/feedback';

@Pipe({
  name: 'textFeedbackTeaser'
})
export class TextFeedbackTeaser {
  transform(feedback: Feedback) : string {
    let textFeedbackText = null;
    if(feedback.textFeedbacks && feedback.textFeedbacks.length > 0) {
      for(let textFeedback of feedback.textFeedbacks) {
        if(textFeedback.text !== "") {
          textFeedbackText = textFeedback.text;
          break;
        }
      }
    }

    if(textFeedbackText) {
      let limit = 30;
      let trail = '...';

      return textFeedbackText.length > limit ? textFeedbackText.substring(0, limit) + trail : textFeedbackText;
    } else {
      return "";
    }
  }
}
