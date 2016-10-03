import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Application} from '../models/applications/application';

@Injectable()
export class ApplicationService {

  constructor(private http:Http) {
  }

  all():Observable<Application> {
    return this.http.get('/services/mocks/applications.json')
      .map((res: Response) => res.json())
      .catch(this.handleError);
  }

  find(id:number):Observable<Application> {
    return this.http.get('/services/mocks/application5.json')
      .map((res: Response) => res.json())
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

