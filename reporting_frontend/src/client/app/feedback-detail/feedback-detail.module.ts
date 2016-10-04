import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {FeedbackDetailComponent} from './feedback-detail.component';
import {ParameterValuePipe} from '../shared/pipes/parameter-value.pipe';


@NgModule({
    imports: [CommonModule, RouterModule],
    declarations: [FeedbackDetailComponent, ParameterValuePipe],
    exports: [FeedbackDetailComponent],
    providers: []
})

export class FeedbackDetailModule { }
