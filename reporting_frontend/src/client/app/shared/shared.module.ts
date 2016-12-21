import {NgModule, ModuleWithProviders} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';

import {ToolbarComponent} from './toolbar/index';
import {NavbarComponent} from './navbar/index';
import {NameListService} from './name-list/index';
import {FeedbackListService} from './services/feedback-list.service';
import {FeedbackDetailService} from './services/feedback-detail.service';
import {ApplicationService} from './services/application.service';
import {UserService} from './services/user.service';
import {LoggedInGuard} from './guards/logged-in.guard';
import {ApplicationFilterService} from './services/application-filter.service';

/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [ToolbarComponent, NavbarComponent],
  exports: [ToolbarComponent, NavbarComponent,
    CommonModule, FormsModule, RouterModule]
})
export class SharedModule {
  static forRoot():ModuleWithProviders {
    return {
      ngModule: SharedModule,
      providers: [NameListService, FeedbackListService, FeedbackDetailService, ApplicationService, UserService, LoggedInGuard, ApplicationFilterService]
    };
  }
}
