import {Injectable} from '@angular/core';
import {Http, Headers} from '@angular/http';
import {REPOSITORY_HOST} from './config';
import {Router} from '@angular/router';


@Injectable()
export class UserService {
  private loggedIn = false;

  constructor(private http:Http, private router:Router) {
    this.loggedIn = !!localStorage.getItem('auth_token');
  }

  login(username, password) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    return this.http
      .post(REPOSITORY_HOST + 'authenticate',
        JSON.stringify({"name": username, "password": password}),
        {headers}
      )
      .map(res => res.json())
      .map((res) => {
        if (res.token) {
          localStorage.setItem('auth_token', res.token);
          console.log(res.token);
          this.loggedIn = true;
        }
        return res;
      });
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
