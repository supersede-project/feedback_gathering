import {Parameter} from '../parameters/parameter';
import {Mechanism} from './mechanism';
import {CategoryFeedback} from '../feedbacks/category_feedback';


export class CategoryMechanism extends Mechanism {

    constructor(id: number, type:string, active:boolean, order?:number, canBeActivated?:boolean, parameters?:Parameter[]) {
        super(id, type, active, order, canBeActivated, parameters);
    }

    getOptions(): Parameter[] {
        if(this.getParameterValue('options') !== null && this.getParameterValue('options') !== undefined && this.getParameterValue('options').length > 0
            && this.getParameterValue('options').filter(option => option.key !== 'defaultOption').length > 0) {
            return this.getParameterValue('options').filter(option => option.key !== 'defaultOption').sort((option1, option2) => option1.createdAt > option2.createdAt);
        } else {
            return [];
        }
    }

    getDefaultOptions(): Parameter {
        if(this.getParameterValue('options') !== null && this.getParameterValue('options') !== undefined && this.getParameterValue('options').length > 0
            && this.getParameterValue('options').filter(option => option.key === 'defaultOption').length > 0) {
            return this.getParameterValue('options').filter(option => option.key === 'defaultOption')[0];
        } else {
            return null;
        }
    }

    getContext(): any {
        return {
            title: this.getParameterValue('title'),
            ownAllowed: this.getParameterValue('ownAllowed'),
            ownLabel: this.getParameterValue('ownLabel'),
            breakAfterOption: this.getParameterValue('breakAfterOption') ? true : false,
            options: this.getOptions(),
            defaultOption: this.getDefaultOptions(),
            inputType: this.getParameterValue('multiple') ? 'checkbox' : 'radio',
            multiple: this.getParameterValue('multiple'),
            asDropdown: this.getParameterValue('asDropdown') || false,
            mandatory: this.getParameterValue('mandatory'),
            mandatoryReminder: this.getParameterValue('mandatoryReminder'),
            boxWidth: this.getParameterValue('boxWidth') || '100%',
            boxPaddingLeft: this.getParameterValue('boxPaddingLeft') || '0',
            boxPaddingRight: this.getParameterValue('boxPaddingRight') || '20px'
        }
    }

    getCategoryFeedbacks(): CategoryFeedback[] {
        var inputSelector = this.getInputSelector();
        var selectSelector = this.getSelectSelector();
        var categoryFeedbacks:CategoryFeedback[] = [];

        if(this.getParameterValue('asDropdown')) {
            jQuery(selectSelector + " option:selected").each(function() {
                var parameterId = jQuery(this).val();
                categoryFeedbacks.push(new CategoryFeedback(parameterId, ""));
            });
        } else {
            jQuery(inputSelector).each(function () {
                var input = jQuery(this);

                if((input.attr('type') === 'checkbox' || input.attr('type') === 'radio') && input.is(':checked')) {
                    categoryFeedbacks.push(new CategoryFeedback(input.data('parameter-id'), ""));
                } else if(input.attr('type') === 'text' && input.val() !== "") {
                    categoryFeedbacks.push(new CategoryFeedback(null, input.val()));
                }
            });
        }

        return categoryFeedbacks;
    }

    getInputSelector() {
        return 'section#categoryMechanism' + this.id + '.category-type input';
    }

    getSelectSelector() {
        return 'section#categoryMechanism' + this.id + '.category-type select';
    }
}
