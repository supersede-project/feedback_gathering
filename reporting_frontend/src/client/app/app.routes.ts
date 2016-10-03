import {Routes} from '@angular/router';

import {FeedbackListRoutes} from './feedback-list/feedback-list.routes';
import {FeedbackDetailRoutes} from './feedback-detail/feedback-detail.routes';

export const routes:Routes = [
  ...FeedbackListRoutes,
  ...FeedbackDetailRoutes
];
