import { Route } from '@angular/router';
import {FeedbackDetailComponent} from './feedback-detail.component';
import {LoggedInGuard} from '../shared/guards/logged-in.guard';

export const FeedbackDetailRoutes: Route[] = [
  {
    path: 'feedbacks/:applicationId/:id',
    component: FeedbackDetailComponent,
    canActivate: [LoggedInGuard]
  }
];
