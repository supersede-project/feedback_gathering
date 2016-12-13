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
import {AttachmentMechanism} from '../shared/models/mechanisms/attachment_mechanism';
import {AudioMechanism} from '../shared/models/mechanisms/audio_mechanism';
import {CategoryMechanism} from '../shared/models/mechanisms/category_mechanism';
import {REPOSITORY_HOST, FILE_HOST} from '../shared/services/config';
import { Http, Response, Headers } from '@angular/http';
import {AttachmentFeedback} from '../shared/models/feedbacks/attachment_feedback';


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
  host:string = FILE_HOST;

  constructor(public feedbackListService:FeedbackListService, private route:ActivatedRoute,
              private feedbackService:FeedbackDetailService, private applicationService:ApplicationService,
              private router:Router, private feedbackStatusService:FeedbackStatusService, private location:Location,
              private http: Http) {
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
            }
            this.markAsReadOrUnread(feedback, true);
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
    this.applicationService.find(id, this.feedback.language).subscribe(
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
      for (let textFeedback of this.feedback.textFeedbacks) {
        let textMechanism:TextMechanism = <TextMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === textFeedback.mechanismId)[0];
        textFeedback.mechanism = <TextMechanism>textMechanism;
      }
    }
    if (this.feedback && this.feedback.ratingFeedbacks) {
      for (let ratingFeedback of this.feedback.ratingFeedbacks) {
        let ratingMechanism:RatingMechanism = <RatingMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === ratingFeedback.mechanismId)[0];
        ratingFeedback.mechanism = <RatingMechanism>ratingMechanism;
      }
    }
    if (this.feedback && this.feedback.attachmentFeedbacks) {
      for (let attachmentFeedback of this.feedback.attachmentFeedbacks) {
        attachmentFeedback.downloadLink = REPOSITORY_HOST + attachmentFeedback.path;
        let attachmentMechanism:AttachmentMechanism = <AttachmentMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === attachmentFeedback.mechanismId)[0];
        attachmentFeedback.mechanism = <AttachmentMechanism>attachmentMechanism;
      }
    }
    if (this.feedback && this.feedback.audioFeedbacks) {
      for (let audioFeedback of this.feedback.audioFeedbacks) {
        let audioMechanism:AudioMechanism = <AudioMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === audioFeedback.mechanismId)[0];
        audioFeedback.mechanism = <AudioMechanism>audioMechanism;
      }
    }
    if (this.feedback && this.feedback.categoryFeedbacks) {
      for (let categoryFeedback of this.feedback.categoryFeedbacks) {
        var matchingCategoryMechanism = null;
        for (let categoryMechanism of this.configuration.mechanisms.filter(mechanism => mechanism.type === 'CATEGORY_TYPE')) {
          let optionsParameter = categoryMechanism.parameters.filter(parameter => parameter.key === 'options')[0];
          if (optionsParameter.value.filter(parameter => parameter.id === categoryFeedback.parameterId).length > 0) {
            matchingCategoryMechanism = categoryMechanism;
            break;
          }
        }
        categoryFeedback.mechanism = <CategoryMechanism>matchingCategoryMechanism;
      }
    }
  }

  showNextFeedback() {
    if (this.feedback.id !== this.feedbacks[this.feedbacks.length - 1].id) {
      this.feedback = this.feedbacks[this.getCurrentFeedbackIndex() + 1];
      this.loadApplication(this.feedback.applicationId, this.feedback.configurationId);
      this.markAsReadOrUnread(this.feedback, true);
    }
  }

  showPreviousFeedback() {
    if (this.feedback.id !== this.feedbacks[0].id) {
      this.feedback = this.feedbacks[this.getCurrentFeedbackIndex() - 1];
      this.loadApplication(this.feedback.applicationId, this.feedback.configurationId);
      this.markAsReadOrUnread(this.feedback, true);
    }
  }

  markAsReadOrUnread(feedback:Feedback, read:boolean):void {
    let applicationId = feedback.applicationId;
    if(feedback.feedbackStatuses && feedback.feedbackStatuses.length > 0 && feedback.feedbackStatuses.filter(feedbackStatus => feedbackStatus.status === 'read' || feedbackStatus.status === 'unread').length > 0) {
      let feedbackStatus = feedback.feedbackStatuses.filter(feedbackStatus => feedbackStatus.status === 'read' || feedbackStatus.status === 'unread')[0];
      this.feedbackStatusService.updateReadStatus(read, feedbackStatus.id, feedbackStatus.feedbackId, applicationId).subscribe(
        result => {
          if (!read) {
            this.location.back();
          }
        },
        error => {
          console.log(error);
        }
      );
    }
  }

  downloadFile(attachmentFeedback:AttachmentFeedback) {
    var headers = new Headers();
    headers.append('Authorization', localStorage.getItem('auth_token'));
    headers.append('accept', 'application/octet-stream');

    var url = attachmentFeedback.downloadLink;

    this.http.get(
      url, {headers: headers}).subscribe(
      (response) => {
        console.log(response);
        var filename = attachmentFeedback.name;
        var mediaType = 'application/octet-stream';
        //var blob = new Blob([response._body], {type: mediaType});
        //saveAs(blob, filename)
      }
    );
  }

  private getCurrentFeedbackIndex():number {
    let feedbackInArray = this.feedbacks.filter(feedback => feedback.id === this.feedback.id)[0];
    return this.feedbacks.indexOf(feedbackInArray);
  }
}
