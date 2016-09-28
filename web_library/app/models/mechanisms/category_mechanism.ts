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
            inputType: this.getParameterValue('multiple') ? 'checkbox' : 'radio'
        }
    }

    getCategoryFeedbacks(): CategoryFeedback[] {
        var inputSelector = this.getInputSelector();
        var categoryFeedbacks:CategoryFeedback[] = [];

        jQuery(inputSelector).each(function () {
            var input = jQuery(this);

            if((input.attr('type') === 'checkbox' || input.attr('type') === 'radio') && input.is(':checked')) {
                categoryFeedbacks.push(new CategoryFeedback(input.data('parameter-id'), ""));
            } else if(input.attr('type') === 'text' && input.val() !== "") {
                categoryFeedbacks.push(new CategoryFeedback(null, input.val()));
            }
        });

        return categoryFeedbacks;
    }

    getInputSelector() {
        return 'section#categoryMechanism' + this.id + '.category-type input';
    }

    coordinateOwnInputAndRadioBoxes() {
        // set constraints between radio boxes and own input fields
        if(this.active && this.getParameterValue('multiple') === 0 && this.getParameterValue('ownAllowed') === 1) {
            var ownTextInput = jQuery(this.getInputSelector() + '.own-category');
            var radioInputs = jQuery(this.getInputSelector() + '[type="radio"]');

            // uncheck all radios if text input gets text
            ownTextInput.on('keyup change', function() {
                if(jQuery(this).val().length > 0) {
                    radioInputs.each(function() {
                        jQuery(this).prop('checked', false);
                    });
                }
            });

            // empty text input if radio input is checked
            radioInputs.on('change', function() {
                if(jQuery(this).is(':checked')) {
                    ownTextInput.val("");
                }
            });
        }
    }
}
