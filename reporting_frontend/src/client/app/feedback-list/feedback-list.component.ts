import {Component, OnInit} from '@angular/core';
import {Feedback} from '../../models/feedbacks/feedback';
import {FeedbackListService} from '../../services/feedback-list.service';
import {Application} from '../../models/applications/application';
import {ApplicationService} from '../../services/application.service';

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
  applications:Application[] = [];

  constructor(public feedbackListService:FeedbackListService, private applicationService:ApplicationService) {}

  ngOnInit() {
    this.getFeedbacks();
    this.getApplications();
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

  getApplications() {
    this.applicationService.all().subscribe(
      applications => {
        for(var application of applications) {
          application.filterActive = true;
        }
        this.applications = applications;
      },
      error => this.errorMessage = <any>error
    );
  }

  sortFeedbacks(field:string, ascending:boolean = true) {
    var feedbacks = this.feedbacks.sort(function (feedbackA, feedbackB) {
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

  clickedApplicationFilter(application) {
    application.filterActive = !application.filterActive;
  }
}
