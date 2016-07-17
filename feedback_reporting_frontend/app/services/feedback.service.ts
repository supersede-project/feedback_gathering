/**
 * Created by flo on 14.07.16.
 */
import { Injectable } from 'angular2/core';
import { Http, Response, Headers } from 'angular2/http';
import 'rxjs/add/operator/map'
import { Observable } from 'rxjs/Observable';
import { Feedback } from '../models/Feedback';
import { ServerConfiguration } from './ServerConfiguration';

@Injectable()
export class FeedbackService{
    private url: string;
    private headers: Headers;

    constructor(private http: Http, private configuration: ServerConfiguration){
        this.url = configuration.ServerWithApiUrl;

        this.headers = new Headers();
        this.headers.append('Accept', 'application/json');
    }

    public GetFeedbacks = (application: string): Observable<Feedback[]> => {
        return this.http.get(this.url + application + "/feedbacks")
            .map((response: Response) => <Feedback[]>response.json());
    }

    private handleError(error: Response){
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
}