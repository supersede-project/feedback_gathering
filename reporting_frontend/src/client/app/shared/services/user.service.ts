import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import {REPOSITORY_HOST} from './config';
import {Router} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {ApiUser} from '../models/api_user';

declare function camelizer(obj: any): any;


@Injectable()
export class UserService {
  private loggedIn = false;

  constructor(private http:Http, private router:Router) {
    this.loggedIn = !!localStorage.getItem('auth_token');
  }

  loginOnRepository(username, password) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    return this.http
      .post(REPOSITORY_HOST + 'authenticate',
        JSON.stringify({"name": username, "password": password}),
        {headers}
      )
      .map((res: Response) => camelizer(res.json()))
      .map((res) => {
        if (res.token) {
          localStorage.setItem('auth_token', res.token);
          this.loggedIn = true;
        }
        return res;
      }).catch((error:any) => Observable.throw(error.json().error || 'Server error'));
  }

  getUsers():Observable<ApiUser[]> {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Authorization', localStorage.getItem('auth_token'));

    return this.http.get(REPOSITORY_HOST + 'en/api_users', {headers: headers})
      .map((res: Response) => camelizer(res.json()))
      .catch((error:any) => Observable.throw(error || 'Server error'));
  }

  logout() {
    localStorage.removeItem('auth_token');
    this.loggedIn = false;
    this.router.navigate(['/login']);
  }

  isLoggedIn() {
    return this.loggedIn;
  }
}
