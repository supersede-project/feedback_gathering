import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import {REPOSITORY_HOST} from './config';
import {Router} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {ApiUser} from '../models/api_user';
import {NotificationSetting} from '../models/settings/notification_setting';

declare function camelizer(obj:any):any;


@Injectable()
export class NotificationSettingService {
  private loggedIn = false;

  constructor(private http:Http) {
  }

  // /repository/en/notification_settings
  get() {
    return this.http.get(REPOSITORY_HOST + 'en/notification_settings', {headers: this.getHeaders()})
      .map((res:Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error || 'Server error'));
  }

  // /repository/en/notification_settings/:id
  find(id:number) {
    return this.http.get(REPOSITORY_HOST + 'en/notification_settings/' + id, {headers: this.getHeaders()})
      .map((res:Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error || 'Server error'));
  }

  // /repository/en/notification_settings
  create(notificationSetting:NotificationSetting) {
    return this.http
      .post(REPOSITORY_HOST + 'en/notification_settings/',
        JSON.stringify(notificationSetting.toUnderscore()),
        {headers: this.getHeaders()}
      )
      .map((res:Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  // /repository/en/notification_settings/:id
  update(notificationSetting:NotificationSetting) {
    return this.http
      .put(REPOSITORY_HOST + 'en/notification_settings/' + notificationSetting.id,
        JSON.stringify(notificationSetting.toUnderscore()),
        {headers: this.getHeaders()}
      )
      .map((res:Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  // /repository/en/notification_settings/:id
  destroy(notificationSetting:NotificationSetting) {
    return this.http
      .delete(REPOSITORY_HOST + 'en/notification_settings/' + notificationSetting.id,
        {headers: this.getHeaders()}
      )
      .map((res:Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  getHeaders() {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Authorization', localStorage.getItem('auth_token'));
    return headers;
  }
}
