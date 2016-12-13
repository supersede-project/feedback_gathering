import { Pipe, PipeTransform } from '@angular/core';


@Pipe({name: 'optionValue'})
export class OptionValuePipe implements PipeTransform {
  transform(categoryFeedbackMechanism:any, parameterId:string, categoryFeedbackText:string): string {
    console.log(categoryFeedbackMechanism);
    console.log(categoryFeedbackMechanism.parameters);

    if(categoryFeedbackText) {
      return categoryFeedbackText;
    }

    let parameterdId = +parameterdId;

    let optionsParameter = categoryFeedbackMechanism.parameters.filter(parameter => parameter.key === 'options')[0];
    if (optionsParameter.value.filter(parameter => parameter.id === parameterId).length > 0) {
      return optionsParameter.value.filter(parameter => parameter.id === parameterId)[0].value;
    }
  }
}
