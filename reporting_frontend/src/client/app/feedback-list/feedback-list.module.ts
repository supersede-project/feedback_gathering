import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {FeedbackListComponent} from './feedback-list.component';
import {ApplicationNameByIdPipe} from '../shared/pipes/application-name-by-id.pipe';
import {TruncatePipe} from '../shared/pipes/truncate.pipe';
import {FeedbackStatusService} from '../shared/services/feedback-status.service';

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [FeedbackListComponent, ApplicationNameByIdPipe, TruncatePipe],
  exports: [FeedbackListComponent],
  providers: [FeedbackStatusService]
})

export class FeedbackListModule {
}
