import {Route} from '@angular/router';
import {FeedbackListComponent} from './index';
import {LoggedInGuard} from '../shared/guards/logged-in.guard';

export const FeedbackListRoutes:Route[] = [
  {
    path: '',
    component: FeedbackListComponent,
    canActivate: [] //[LoggedInGuard]
  }
];
