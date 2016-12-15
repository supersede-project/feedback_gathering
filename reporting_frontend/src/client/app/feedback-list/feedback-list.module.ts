import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {FeedbackListComponent} from './feedback-list.component';
import {ApplicationNameByIdPipe} from '../shared/pipes/application-name-by-id.pipe';
import {TruncatePipe} from '../shared/pipes/truncate.pipe';
import {FeedbackStatusService} from '../shared/services/feedback-status.service';
import {TextFeedbackTeaser} from '../shared/pipes/text-feedback-teaser.pipe';

@NgModule({
  imports: [CommonModule, RouterModule, FormsModule],
  declarations: [FeedbackListComponent, ApplicationNameByIdPipe, TruncatePipe, TextFeedbackTeaser],
  exports: [FeedbackListComponent],
  providers: [FeedbackStatusService]
})

export class FeedbackListModule {
}
