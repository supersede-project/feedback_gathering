/**
 * Created by flo on 14.07.16.
 */
import {Injectable} from '@angular/core'

@Injectable()
export class ServerConfiguration {
    public Server: string = "http://ec2-54-175-37-30.compute-1.amazonaws.com/";
    public ApiUrl: string = "feedback_repository/";
    public ServerWithApiUrl = this.Server + this.ApiUrl;
}
