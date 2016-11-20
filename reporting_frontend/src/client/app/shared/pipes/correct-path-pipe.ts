import { Pipe, PipeTransform } from '@angular/core';


@Pipe({name: 'correctPathPipe'})
export class CorrectPathPipe implements PipeTransform {
  transform(path:string, args:string): string {
    return path.replace('opt/bitnami/apache-tomcat/webapps/', '');
  }
}

