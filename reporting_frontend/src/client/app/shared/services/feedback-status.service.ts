import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {REPOSITORY_HOST} from './config';
import {FeedbackStatus} from '../models/feedbacks/feedback_status';

@Injectable()
export class FeedbackStatusService {

  constructor(private http: Http) {}

  updateReadStatus(read:boolean, feedbackStatusId, feedbackId, applicationId) {
    let status = read ? 'read' : 'unread';
    let apiUserId = +localStorage.getItem('api_user_id');
    let feedbackStatus = new FeedbackStatus(feedbackStatusId, apiUserId, feedbackId, status);
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Authorization', localStorage.getItem('auth_token'));

    return this.http.put(REPOSITORY_HOST + 'en/applications/' + applicationId + '/feedbacks/' + feedbackId + '/feedback_statuses/' + feedbackStatusId, JSON.stringify(feedbackStatus), { headers: headers })
      .map((res: Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error || 'Server error'));
  }
}


