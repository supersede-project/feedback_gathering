import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {FeedbackDetailComponent} from './feedback-detail.component';
import {ParameterValuePipe} from '../shared/pipes/parameter-value.pipe';
import {FeedbackStatusService} from '../shared/services/feedback-status.service';


@NgModule({
    imports: [CommonModule, RouterModule],
    declarations: [FeedbackDetailComponent, ParameterValuePipe],
    exports: [FeedbackDetailComponent],
    providers: [FeedbackStatusService]
})

export class FeedbackDetailModule { }
