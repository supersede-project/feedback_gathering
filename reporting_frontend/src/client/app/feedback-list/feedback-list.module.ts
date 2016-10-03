import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FeedbackListComponent } from './feedback-list.component';
import {FeedbackListService} from '../../services/feedback-list.service';
import {ApplicationService} from '../../services/application.service';
import {ApplicationNameByIdPipe} from '../../pipes/application-name-by-id.pipe';

@NgModule({
    imports: [CommonModule, RouterModule],
    declarations: [FeedbackListComponent, ApplicationNameByIdPipe],
    exports: [FeedbackListComponent],
    providers: [FeedbackListService, ApplicationService]
})

export class FeedbackListModule { }
