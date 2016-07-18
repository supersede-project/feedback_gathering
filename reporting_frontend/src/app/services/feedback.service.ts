/**
 * Created by flo on 14.07.16.
 */
import { Injectable, EventEmitter } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import 'rxjs/add/operator/map'
import { Observable } from 'rxjs/Observable';
import { Feedback } from '../models/Feedback';
import { ServerConfiguration } from './ServerConfiguration';

@Injectable()
export class FeedbackService{
    private url: string;
    private headers: Headers;

    public feedbackListEvent: EventEmitter<Feedback[]> = new EventEmitter();
    public selectedFeedbackEvent: EventEmitter<Feedback> = new EventEmitter();

    constructor(private http: Http, private configuration: ServerConfiguration){
        this.url = configuration.ServerWithApiUrl;

        this.headers = new Headers();
        this.headers.append('Accept', 'application/json');
    }

    public GetFeedbacks(application: string){
      return this.http.get(this.url + application + "/feedbacks")
        .map((response: Response) => <Feedback[]>response.json())
        .subscribe((data) => this.feedbackListEvent.emit(data))
    }

    public SelectFeedback(feedback: Feedback){
      this.selectedFeedbackEvent.emit(feedback);
    }

    private handleError(error: Response){
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
    }
}
