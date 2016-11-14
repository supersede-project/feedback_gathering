import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';
import {CategoryFeedback} from '../feedbacks/category_feedback';


export class CategoryMechanism extends Mechanism {

    constructor(id: number, type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }

    getOptions(): Parameter[] {
        return this.getParameterValue('options');
    }

    getContext(): any {
        return {
            title: this.getParameterValue('title'),
            ownAllowed: this.getParameterValue('ownAllowed'),
            ownLabel: this.getParameterValue('ownLabel'),
            multiple: this.getParameterValue('multiple'),
            breakAfterOption: this.getParameterValue('breakAfterOption') ? true : false,
            options: this.getOptions(),
            inputType: this.getParameterValue('multiple') ? 'checkbox' : 'radio',
            mandatory: this.getParameterValue('mandatory'),
            mandatoryReminder: this.getParameterValue('mandatoryReminder')
        }
    }
}
