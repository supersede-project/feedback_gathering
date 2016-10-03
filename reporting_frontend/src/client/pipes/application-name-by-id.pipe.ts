import { Pipe, PipeTransform } from '@angular/core';


@Pipe({name: 'applicationNameById'})
export class ApplicationNameByIdPipe implements PipeTransform {
  transform(id:any, applications:any, defaultValue:any): string {
    var result = applications.filter(application => application.id === id)[0];
    if(result) {
      return result.name
    } else {
      return defaultValue;
    }
  }
}
