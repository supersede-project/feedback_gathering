import { Route } from '@angular/router';
import {LoggedInGuard} from '../shared/guards/logged-in.guard';
import {SettingsComponent} from './settings.component';


export const SettingsRoutes: Route[] = [
  {
    path: 'settings',
    component: SettingsComponent,
    canActivate: [LoggedInGuard]
  }
];
