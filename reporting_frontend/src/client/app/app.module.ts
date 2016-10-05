import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {APP_BASE_HREF} from '@angular/common';
import {RouterModule} from '@angular/router';
import {HttpModule} from '@angular/http';
import {AppComponent} from './app.component';
import {routes} from './app.routes';

import {SharedModule} from './shared/shared.module';
import {FeedbackListModule} from './feedback-list/feedback-list.module';
import {FeedbackDetailModule} from './feedback-detail/feedback-detail.module';
import {SettingsModule} from './settings/settings.module';
import {LoginModule} from './login/login.module';


@NgModule({
  imports: [BrowserModule, HttpModule, RouterModule.forRoot(routes), FeedbackListModule, FeedbackDetailModule,
    SharedModule.forRoot(), SettingsModule, LoginModule],
  declarations: [AppComponent],
  providers: [{
    provide: APP_BASE_HREF,
    useValue: '<%= APP_BASE %>'
  }],
  bootstrap: [AppComponent]

})

export class AppModule {
}
