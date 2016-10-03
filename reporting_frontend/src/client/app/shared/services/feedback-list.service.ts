import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Feedback} from '../models/feedbacks/feedback';

@Injectable()
export class FeedbackListService {

  constructor(private http: Http) {}

  get(): Observable<Feedback[]> {
    return this.http.get('http://ec2-54-175-37-30.compute-1.amazonaws.com/examples/prod/app/shared/services/mocks/feedbacks.json')
                    .map((res: Response) => res.json())
                    .catch(this.handleError);
  }

  /**
    * Handle HTTP error
    */
  private handleError (error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}

