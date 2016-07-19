import { bootstrap } from '@angular/platform-browser-dynamic';
import { enableProdMode } from '@angular/core';
import { AppComponent, environment } from './app/';
import {HTTP_PROVIDERS} from '@angular/http'
import {ServerConfiguration} from './app/services/ServerConfiguration'
import {disableDeprecatedForms, provideForms} from '@angular/forms'

if (environment.production) {
  enableProdMode();
}

bootstrap(AppComponent,
  [HTTP_PROVIDERS, ServerConfiguration, disableDeprecatedForms(), provideForms()]
);

