import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {FeedbackDetailComponent} from './feedback-detail.component';
import {FeedbackDetailService} from '../../services/feedback-detail.service';
import {ApplicationService} from '../../services/application.service';
import {ParameterValuePipe} from '../../pipes/parameter-value.pipe';

@NgModule({
    imports: [CommonModule, RouterModule],
    declarations: [FeedbackDetailComponent, ParameterValuePipe],
    exports: [FeedbackDetailComponent],
    providers: [FeedbackDetailService, ApplicationService]
})

export class FeedbackDetailModule { }
