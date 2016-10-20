import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfigurationInterface} from '../shared/models/configurations/configuration_interface';
import {Application} from '../shared/models/applications/application';
import {Feedback} from '../shared/models/feedbacks/feedback';
import {FeedbackDetailService} from '../shared/services/feedback-detail.service';
import {ApplicationService} from '../shared/services/application.service';
import {TextMechanism} from '../shared/models/mechanisms/text_mechanism';
import {RatingMechanism} from '../shared/models/mechanisms/rating_mechanism';
import {FeedbackListService} from '../shared/services/feedback-list.service';
import {FeedbackStatusService} from '../shared/services/feedback-status.service';
import {FeedbackStatus} from '../shared/models/feedbacks/feedback_status';
import {Location} from '@angular/common';


@Component({
  moduleId: module.id,
  selector: 'sd-feedback-detail',
  templateUrl: 'feedback-detail.component.html',
  styleUrls: ['feedback-detail.component.css']
})
export class FeedbackDetailComponent implements OnInit {
  feedback:Feedback;
  feedbacks:Feedback[] = [];
  application:Application;
  configuration:ConfigurationInterface;
  errorMessage:string;
  host:string = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/';

  constructor(public feedbackListService:FeedbackListService, private route:ActivatedRoute,
              private feedbackService:FeedbackDetailService, private applicationService:ApplicationService,
              private router:Router, private feedbackStatusService:FeedbackStatusService, private location: Location) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params.hasOwnProperty('applicationId') && params['applicationId'] && params.hasOwnProperty('id') && params['id']) {
        let applicationId = +params['applicationId'];
        let id = +params['id'];
        this.feedbackService.find(applicationId, id).subscribe(
          feedback => {
            this.feedback = <Feedback>feedback;
            if (feedback && feedback.applicationId) {
              this.loadApplication(feedback.applicationId, feedback.configurationId);
              this.getFeedbackStatuses(feedback.applicationId);
            }
          },
          error => this.errorMessage = <any>error
        );
        this.getFeedbacks(applicationId);
      }
    });
  }

  getFeedbacks(applicationId:number) {
    this.feedbackListService.get(applicationId)
      .subscribe(
        feedbacks => {
          this.feedbacks = feedbacks;
        },
        error => {
          console.log(error);
        }
      );
  }

  loadApplication(id:number, configurationId:number) {
    this.applicationService.find(id).subscribe(
      application => {
        this.application = application;
        this.configuration = application.configurations.filter(configuration => configuration.id === configurationId)[0];
        this.populateConfigurationData();
      },
      error => this.errorMessage = <any>error
    );
  }

  populateConfigurationData() {
    if (this.feedback && this.feedback.textFeedbacks) {
      for (var textFeedback of this.feedback.textFeedbacks) {
        let textMechanism:TextMechanism = <TextMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === textFeedback.mechanismId)[0];
        textFeedback.mechanism = <TextMechanism>textMechanism;
      }
    }
    if (this.feedback && this.feedback.ratingFeedbacks) {
      for (var ratingFeedback of this.feedback.ratingFeedbacks) {
        let ratingMechanism:RatingMechanism = <RatingMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === ratingFeedback.mechanismId)[0];
        ratingFeedback.mechanism = <RatingMechanism>ratingMechanism;
      }
    }
  }

  showNextFeedback() {
    if (this.feedback.id !== this.feedbacks[this.feedbacks.length - 1].id) {
      this.feedback = this.feedbacks[this.getCurrentFeedbackIndex() + 1];
      this.markAsReadOrUnread(this.feedback, true);
    }
  }

  showPreviousFeedback() {
    if (this.feedback.id !== this.feedbacks[0].id) {
      this.feedback = this.feedbacks[this.getCurrentFeedbackIndex() - 1];
      this.markAsReadOrUnread(this.feedback, true);
    }
  }

  markAsReadOrUnread(feedback:Feedback, read:boolean):void {
    if(!feedback.personalFeedbackStatus) {
      return;
    }
    let applicationId = feedback.applicationId;
    let feedbackStatus = feedback.personalFeedbackStatus;
    this.feedbackStatusService.updateReadStatus(read, feedbackStatus.id, feedbackStatus.feedbackId, applicationId).subscribe(
      result => {
        if(!read) {
          this.location.back();
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  getFeedbackStatuses(applicationId:number) {
    this.feedbackStatusService.get(applicationId)
      .subscribe(
        feedbackStatuses => {
          let currentUserId = +localStorage.getItem('api_user_id');
          let feedbackStatus = feedbackStatuses.filter(feedbackStatus => {
            return feedbackStatus.feedbackId === this.feedback.id && feedbackStatus.apiUserId === currentUserId
          })[0];
          this.populateStatusData(feedbackStatus);
          // populate for other feedbacks as well
          let currentUserFeedbackStatuses = feedbackStatuses.filter(feedbackStatus => feedbackStatus.apiUserId === currentUserId);
          this.populateStatusDataForAll(currentUserFeedbackStatuses);
        },
        error => {
          console.log(error);
        });
  }

  /**
   * combines repository feedback with the personal feedback status
   */
  populateStatusData(feedbackStatus:FeedbackStatus) {
    this.feedback.personalFeedbackStatus = feedbackStatus;
    this.feedback.read = this.feedback.personalFeedbackStatus.status === 'read';
    this.markAsReadOrUnread(this.feedback, true);
  }

  populateStatusDataForAll(feedbackStatuses:FeedbackStatus[]) {
    for(var feedback of this.feedbacks) {
      feedback.personalFeedbackStatus = feedbackStatuses.filter(feedbackStatus => feedbackStatus.feedbackId === feedback.id)[0];
      if(feedback.personalFeedbackStatus) {
        feedback.read = feedback.personalFeedbackStatus.status === 'read';
      }
    }
  }

  private getCurrentFeedbackIndex():number {
    let feedbackInArray = this.feedbacks.filter(feedback => feedback.id === this.feedback.id)[0];
    return this.feedbacks.indexOf(feedbackInArray);
  }
}
