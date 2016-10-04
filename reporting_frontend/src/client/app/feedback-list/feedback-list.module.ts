import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {FeedbackListComponent} from './feedback-list.component';
import {ApplicationNameByIdPipe} from '../shared/pipes/application-name-by-id.pipe';

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [FeedbackListComponent, ApplicationNameByIdPipe],
  exports: [FeedbackListComponent],
  providers: []
})

export class FeedbackListModule {
}
