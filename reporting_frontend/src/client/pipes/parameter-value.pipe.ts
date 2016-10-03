import { Pipe, PipeTransform } from '@angular/core';


@Pipe({name: 'parameterValue'})
export class ParameterValuePipe implements PipeTransform {
  transform(mechanism:any, args:string): string {
    console.log(mechanism);
    var key = args;
    if(mechanism && mechanism.parameters) {
      var filteredArray = mechanism.parameters.filter(parameter => parameter.key === key);
      if(filteredArray.length > 0) {
        return filteredArray[0].value;
      } else {
        return null;
      }
    } else {
      return "";
    }
  }
}
