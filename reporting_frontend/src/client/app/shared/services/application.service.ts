import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Application} from '../models/applications/application';
import {ORCHESTRATOR_HOST, RAILS_ORCHESTRATOR_HOST} from './config';

declare function camelizer(obj: any): any;

@Injectable()
export class ApplicationService {

  constructor(private http:Http) {
  }

  all():Observable<Application[]> {
    return this.http.get(ORCHESTRATOR_HOST + 'en/applications')
      .map((res: Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  find(id:number):Observable<Application> {
    return this.http.get(ORCHESTRATOR_HOST + 'en/applications/' + id)
      .map((res: Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  toggleState(id:number):Observable<Application> {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Authorization', localStorage.getItem('auth_token'));

    return this.http.put(RAILS_ORCHESTRATOR_HOST + 'en/applications/' + id + '/toggle_state', JSON.stringify(''), { headers: headers })
      .map((res: Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error || 'Server error'));
  }
}

