import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Application} from '../models/applications/application';
import {ORCHESTRATOR_HOST} from './config';

@Injectable()
export class ApplicationService {

  constructor(private http:Http) {
  }

  all():Observable<Application[]> {
    return this.http.get(ORCHESTRATOR_HOST + 'en/applications')
      .map((res: Response) => res.json())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  find(id:number):Observable<Application> {
    return this.http.get(ORCHESTRATOR_HOST + 'en/applications/' + id)
      .map((res: Response) => res.json())
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }
}

