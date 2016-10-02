import { Routes } from '@angular/router';

import { AboutRoutes } from './about/index';
import { HomeRoutes } from './home/index';
import { FeedbackListRoutes } from './feedback-list/feedback-list.routes';
import {FeedbackDetailRoutes} from './feedback-detail/feedback-detail.routes';

export const routes: Routes = [
  ...HomeRoutes,
  ...AboutRoutes,
  ...FeedbackListRoutes,
  ...FeedbackDetailRoutes
];
