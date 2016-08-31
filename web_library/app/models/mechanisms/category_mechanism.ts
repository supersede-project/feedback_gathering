import {Parameter} from './parameters/parameter';
import {Mechanism} from './mechanism';
import {CategoryFeedback} from '../feedbacks/category_feedback';
import {Category} from '../feedbacks/category';
import {CategoryType} from '../feedbacks/category_type';


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

    getCategoryFeedback(): CategoryFeedback {
        var inputSelector = this.getInputSelector();
        var thisCategoryMechanism = this;
        var categories:Category[] = [];

        jQuery(inputSelector).each(function () {
            var input = jQuery(this);

            if((input.attr('type') === 'checkbox' || input.attr('type') === 'radio') && input.is(':checked')) {
                var categoryKey = input.val();
                var categoryValue = jQuery('section#categoryMechanism' + thisCategoryMechanism.id + '.category-type label[for="option' + categoryKey + '"]').text().trim();
                var categoryType = new CategoryType(categoryKey, categoryValue);
                var category:Category = new Category(thisCategoryMechanism.id, null, null, categoryType);
                categories.push(category);
            } else if(input.attr('type') === 'text' && input.val() !== "") {
                var category:Category = new Category(thisCategoryMechanism.id, input.val(), null, null);
                categories.push(category);
            }
        });

        return new CategoryFeedback(this.id, categories);
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
