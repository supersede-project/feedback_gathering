import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FeedbackListComponent } from './feedback-list.component';
import {FeedbackListService} from '../../services/feedback-list.service';

@NgModule({
    imports: [CommonModule],
    declarations: [FeedbackListComponent],
    exports: [FeedbackListComponent],
    providers: [FeedbackListService]
})

export class FeedbackListModule { }
