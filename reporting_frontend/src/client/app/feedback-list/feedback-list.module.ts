import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FeedbackListComponent } from './feedback-list.component';
import {FeedbackListService} from '../../services/feedback-list.service';

@NgModule({
    imports: [CommonModule, RouterModule],
    declarations: [FeedbackListComponent],
    exports: [FeedbackListComponent],
    providers: [FeedbackListService]
})

export class FeedbackListModule { }
