import { Pipe, PipeTransform } from '@angular/core';


@Pipe({name: 'optionValue'})
export class OptionValuePipe implements PipeTransform {
  transform(categoryFeedbackMechanism:any, parameterId:string, categoryFeedbackText:string): string {
    if(categoryFeedbackText) {
      return categoryFeedbackText;
    }

    let parameterIdNumber:number = +parameterId;

    let optionsParameter = categoryFeedbackMechanism.parameters.filter(parameter => parameter.key === 'options')[0];
    if (optionsParameter.value.filter(parameter => parameter.id === parameterIdNumber).length > 0) {
      return optionsParameter.value.filter(parameter => parameter.id === parameterIdNumber)[0].value;
    } else {
      return "";
    }
  }
}
