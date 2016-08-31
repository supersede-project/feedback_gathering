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

    private getInputSelector() {
        return 'section#categoryMechanism' + this.id + '.category-type input';
    }
}
