import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Feedback} from '../models/feedbacks/feedback';
import {REPOSITORY_HOST} from '../../../../../dist/tmp/app/shared/services/config';

@Injectable()
export class FeedbackDetailService {

  constructor(private http:Http) {
  }

  find(id:number):Observable<Feedback> {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Authorization', localStorage.getItem('auth_token'));

    return this.http.get(REPOSITORY_HOST + 'en/feedbacks/' + id, { headers: headers })
      .map((res: Response) => <Feedback>(<Feedback[]>res.json()).filter(feedback => feedback.id === id)[0])
      .catch(this.handleError);
  }

  /**
   * Handle HTTP error
   */
  private handleError(error:any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}

