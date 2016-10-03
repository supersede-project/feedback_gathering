import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {Feedback} from '../../models/feedbacks/feedback';
import {FeedbackDetailService} from '../../services/feedback-detail.service';
import {ApplicationService} from '../../services/application.service';
import {Application} from '../../models/applications/application';
import {ConfigurationInterface} from '../../models/configurations/configuration_interface';
import {RatingMechanism} from '../../models/mechanisms/rating_mechanism';

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

  constructor(private route:ActivatedRoute, private feedbackService:FeedbackDetailService, private applicationService:ApplicationService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params.id) {
        let id = +params['id'];
        this.feedbackService.find(id).subscribe(
          feedback => {
            this.feedback = <Feedback>feedback;
            this.loadApplication(feedback.applicationId, feedback.configurationId);
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
    for(var ratingFeedback of this.feedback.ratingFeedbacks) {
      var mechanism:RatingMechanism = <RatingMechanism>this.configuration.mechanisms.filter(mechanism => mechanism.id === ratingFeedback.mechanismId)[0];
      ratingFeedback.mechanism = mechanism;
    }
  }
}
