import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Feedback} from '../models/feedbacks/feedback';
import {REPOSITORY_HOST} from './config';

@Injectable()
export class FeedbackDetailService {

  constructor(private http:Http) {
  }

  find(applicationId:number, id:number):Observable<Feedback> {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Authorization', localStorage.getItem('auth_token'));

    return this.http.get(REPOSITORY_HOST + 'en/applications/' + applicationId + '/feedbacks/' + id, { headers: headers })
      .map((res: Response) => res.json())
      .catch((error:any) => Observable.throw(error || 'Server error'));
  }
}

