import {Injectable} from '@angular/core';
import {Application} from '../models/applications/application';


@Injectable()
export class ApplicationFilterService {
  private application:Application;

  constructor() {
  }

  getApplication(): Application {
    return this.application;
  }

  setApplication(application:Application) {
    this.application = application;
  }
}
