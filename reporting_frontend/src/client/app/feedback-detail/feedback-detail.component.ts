import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import {Feedback} from '../../models/feedbacks/feedback';
import {FeedbackDetailService} from '../../services/feedback-detail.service';

@Component({
  moduleId: module.id,
  selector: 'sd-feedback-detail',
  templateUrl: 'feedback-detail.component.html',
  styleUrls: ['feedback-detail.component.css']
})
export class FeedbackDetailComponent implements OnInit {
  feedback:Feedback;
  errorMessage: string;

  constructor(
    private route: ActivatedRoute,
    private service: FeedbackDetailService) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      if(params.id) {
        let id = +params['id'];
        this.service.find(id).subscribe(
          feedback => this.feedback = feedback,
          error =>  this.errorMessage = <any>error
        );
      }
    });
  }
}
