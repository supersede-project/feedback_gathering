import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {Application} from '../models/applications/application';
import {ORCHESTRATOR_HOST} from './config';

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

  find(id:number, lang:string):Observable<Application> {
    return this.http.get(ORCHESTRATOR_HOST + lang + '/applications/' + id)
      .map((res: Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }
}

