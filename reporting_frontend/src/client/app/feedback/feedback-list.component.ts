import { Component, OnInit } from '@angular/core';
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

  newName: string = '';
  errorMessage: string;
  feedbacks: Feedback[] = [];

  constructor(public feedbackListService: FeedbackListService) {}

  /**
   * Get the names OnInit
   */
  ngOnInit() {
    this.getFeedbacks();
  }

  getFeedbacks() {
    this.feedbackListService.get()
		     .subscribe(
           feedbacks => this.feedbacks = feedbacks,
		       error =>  this.errorMessage = <any>error
		       );
  }
}
