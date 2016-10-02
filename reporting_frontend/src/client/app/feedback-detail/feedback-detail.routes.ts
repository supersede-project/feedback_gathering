import { Route } from '@angular/router';
import {FeedbackDetailComponent} from './feedback-detail.component';

export const FeedbackDetailRoutes: Route[] = [
  {
    path: 'feedback-detail/:id',
    component: FeedbackDetailComponent
  }
];
