/**
 * Created by flo on 14.07.16.
 */
import {Component} from 'angular2/core'
import {AuthorsService} from '../services/authors.service'

@Component({
    selector: 'authors',
    template: `
        <h2>Authors</h2>
        {{title}}
        <ul>
           <li *ngFor="#author of authors">
           {{author}}
            </li> 
        </ul>
        `,
    providers: [AuthorsService]
})
export class AuthorsComponent{
    title: string = 'Title of the authors component';

    authors: string[];
    constructor(authorsService: AuthorsService){
        this.authors = authorsService.getAuthors();
    }
}
