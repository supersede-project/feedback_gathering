/**
 * Created by flo on 14.07.16.
 */
import {Rating} from './Rating'
import {Screenshot} from './Screenshot'

export class Feedback{
    private title: string;
    private application: string;
    private user: string;
    private text: string;
    private created: string;
    private configVersion: number
    private ratings: Rating[]
    private screenshots: Screenshot[]
}
