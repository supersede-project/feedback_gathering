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

  get(applicationId:number): Observable<FeedbackStatus[]> {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Authorization', localStorage.getItem('auth_token'));

    return this.http.get(REPOSITORY_HOST + 'en/applications/' + applicationId + '/states', { headers: headers })
      .map((res: Response) => res.json())
      .catch((error:any) => Observable.throw(error || 'Server error'));
  }

  updateReadStatus(read:boolean, feedbackStatusId, feedbackId, applicationId) {
    let status = read ? 'read' : 'unread';
    let apiUserId = +localStorage.getItem('api_user_id');
    let feedbackStatus = new FeedbackStatus(feedbackStatusId, apiUserId, feedbackId, status);
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Authorization', localStorage.getItem('auth_token'));

    return this.http.put(REPOSITORY_HOST + 'en/applications/' + applicationId + '/states', JSON.stringify(feedbackStatus), { headers: headers })
      .map((res: Response) => res.json())
      .catch((error:any) => Observable.throw(error || 'Server error'));
  }
}


