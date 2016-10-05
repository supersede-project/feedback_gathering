import {Component, OnInit} from '@angular/core';
import {Feedback} from '../shared/models/feedbacks/feedback';
import {Application} from '../shared/models/applications/application';
import {FeedbackListService} from '../shared/services/feedback-list.service';
import {ApplicationService} from '../shared/services/application.service';


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
  sortOrder:{} = {'id': '', 'title': '', 'date': ''};

  constructor(public feedbackListService:FeedbackListService, private applicationService:ApplicationService) {
  }

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
        this.applications = applications;
        for (var application of this.applications) {
          application.filterActive = false;
        }
      },
      error => this.errorMessage = <any>error
    );
  }

  resetSortOrder():void {
    this.sortOrder = {'id': '', 'title': '', 'date': ''};
  }

  sortClicked(field:string) {
    if (this.sortOrder[field] === '') {
      this.resetSortOrder();
      this.sortFeedbacks(field, true);
      this.sortOrder[field] = 'asc';
    } else if (this.sortOrder[field] === 'asc') {
      this.resetSortOrder();
      this.sortFeedbacks(field, false);
      this.sortOrder[field] = 'desc';
    } else if (this.sortOrder[field] === 'desc') {
      this.resetSortOrder();
      this.sortOrder[field] = 'asc';
      this.sortFeedbacks(field, true);
    }
  }

  sortFeedbacks(field:string, ascending:boolean = true) {
    var feedbacks = this.feedbacks.sort(function (feedbackA, feedbackB) {
      if (field === 'date') {
        var dateA = new Date(feedbackA[field]);
        var dateB = new Date(feedbackB[field]);
        return dateA - dateB;
      } else {
        return feedbackA[field] - feedbackB[field];
      }
    });

    if (!ascending) {
      this.filteredFeedbacks = feedbacks.reverse();
    } else {
      this.filteredFeedbacks = feedbacks;
    }
  }

  search(filterString:string) {
    this.filteredFeedbacks = this.feedbacks.filter(item => item.title.toLowerCase().indexOf(filterString.toLowerCase()) !== -1);
  }

  clickedApplicationFilter(application) {
    var wasActive = application.filterActive;

    for (let application of this.applications) {
      application.filterActive = false;
    }
    application.filterActive = !wasActive;
    if (application.filterActive) {
      this.filteredFeedbacks = this.feedbacks.filter(feedback => feedback.applicationId === application.id);
    } else {
      this.filteredFeedbacks = this.feedbacks;
    }
  }
}

















