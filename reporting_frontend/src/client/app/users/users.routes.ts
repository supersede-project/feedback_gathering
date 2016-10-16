import { Route } from '@angular/router';
import {LoggedInGuard} from '../shared/guards/logged-in.guard';
import {UsersComponent} from './users.component';


export const UsersRoutes: Route[] = [
  {
    path: 'users',
    component: UsersComponent,
    canActivate: [] //[LoggedInGuard]
  }
];
