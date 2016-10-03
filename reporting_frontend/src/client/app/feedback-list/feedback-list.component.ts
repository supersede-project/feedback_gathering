import {Component, OnInit} from '@angular/core';
import {Feedback} from '../../models/feedbacks/feedback';
import {FeedbackListService} from '../../services/feedback-list.service';

/**
 * This class represents the lazy loaded HomeComponent.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-feedback-list',
  templateUrl: 'feedback-list.component.html',
  styleUrls: ['feedback-list.component.css'],
})

export class FeedbackListComponent implements OnInit {
  errorMessage:string;
  feedbacks:Feedback[] = [];
  filteredFeedbacks:Feedback[] = [];

  constructor(public feedbackListService:FeedbackListService) {
  }

  /**
   * Get the names OnInit
   */
  ngOnInit() {
    this.getFeedbacks();
  }

  getFeedbacks() {
    this.feedbackListService.get()
      .subscribe(
        feedbacks => {
          this.feedbacks = feedbacks;
          this.filteredFeedbacks = feedbacks;
          this.sortFeedbacks('id', false);
        },
        error => this.errorMessage = <any>error
      );
  }

  sortFeedbacks(field:string, ascending:boolean = true) {
    var feedbacks = this.filteredFeedbacks.sort(function (feedbackA, feedbackB) {
      if(field === 'date') {
        return Date.parse(feedbackA[field]).getTime() - Date.parse(feedbackB[field]).getTime();
      } else {
        return feedbackA[field] - feedbackB[field];
      }
    });

    if(!ascending) {
      this.filteredFeedbacks = feedbacks.reverse();
    } else {
      this.filteredFeedbacks = feedbacks;
    }
  }

  search(filterString:string) {
    this.filteredFeedbacks = this.feedbacks.filter(item => item.title.toLowerCase().indexOf(filterString.toLowerCase()) !== -1);
  }
}
