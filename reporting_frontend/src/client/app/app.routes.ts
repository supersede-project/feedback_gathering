import { Routes } from '@angular/router';

import { AboutRoutes } from './about/index';
import { HomeRoutes } from './home/index';
import { FeedbackListRoutes } from './feedback/feedback-list.routes';

export const routes: Routes = [
  ...HomeRoutes,
  ...AboutRoutes,
  ...FeedbackListRoutes
];
