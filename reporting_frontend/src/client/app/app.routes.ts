import {Routes} from '@angular/router';
import {FeedbackListRoutes} from './feedback-list/feedback-list.routes';
import {FeedbackDetailRoutes} from './feedback-detail/feedback-detail.routes';
import {SettingsRoutes} from './settings/settings.routes';
import {LoginRoutes} from './login/login.routes';

export const routes:Routes = [
  ...FeedbackListRoutes,
  ...FeedbackDetailRoutes,
  ...SettingsRoutes,
  ...LoginRoutes
];
