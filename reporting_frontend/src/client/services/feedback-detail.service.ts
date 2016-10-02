import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Feedback} from '../models/feedbacks/feedback';

@Injectable()
export class FeedbackDetailService {

  constructor(private http:Http) {
  }

  find(id:number):Observable<Feedback> {
    return this.http.get('/services/mocks/feedbacks.json')
      .map((res: Response) => (<Feedback[]>res.json()).filter(feedback => feedback.id === id)[0])
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

