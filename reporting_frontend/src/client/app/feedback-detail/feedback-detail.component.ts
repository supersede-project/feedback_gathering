import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfigurationInterface} from '../shared/models/configurations/configuration_interface';
import {Application} from '../shared/models/applications/application';
import {Feedback} from '../shared/models/feedbacks/feedback';
import {FeedbackDetailService} from '../shared/services/feedback-detail.service';
import {ApplicationService} from '../shared/services/application.service';
import {TextMechanism} from '../shared/models/mechanisms/text_mechanism';
import {RatingMechanism} from '../shared/models/mechanisms/rating_mechanism';


@Component({
  moduleId: module.id,
  selector: 'sd-feedback-detail',
  templateUrl: 'feedback-detail.component.html',
  styleUrls: ['feedback-detail.component.css']
})
export class FeedbackDetailComponent implements OnInit {
  feedback:Feedback;
  application:Application;
  configuration:ConfigurationInterface;
  errorMessage:string;
  host:string = 'http://ec2-54-175-37-30.compute-1.amazonaws.com/';

  constructor(private route:ActivatedRoute, private feedbackService:FeedbackDetailService, private applicationService:ApplicationService, private router:Router) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params.hasOwnProperty('id') && params['id']) {
        let id = +params['id'];
        this.feedbackService.find(id).subscribe(
          feedback => {
            this.feedback = <Feedback>feedback;
            if(feedback && feedback.applicationId) {
              this.loadApplication(feedback.applicationId, feedback.configurationId);
            }
          },
          error => this.errorMessage = <any>error
        );
      }
    });
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
    for(var textFeedback of this.feedback.textFeedbacks) {
      let textMechanism:TextMechanism = <TextMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === textFeedback.mechanismId)[0];
      textFeedback.mechanism = <TextMechanism>textMechanism;
    }
    for(var ratingFeedback of this.feedback.ratingFeedbacks) {
      let ratingMechanism:RatingMechanism = <RatingMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === ratingFeedback.mechanismId)[0];
      ratingFeedback.mechanism = <RatingMechanism>ratingMechanism;
    }
  }

  markAsUnread():void {
    this.router.navigate(['/']);
  }
}
