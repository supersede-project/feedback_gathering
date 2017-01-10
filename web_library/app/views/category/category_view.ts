import {CategoryFeedback} from '../../models/feedbacks/category_feedback';
import {CategoryMechanism} from '../../models/mechanisms/category_mechanism';
import {MechanismView} from '../mechanism_view';


export class CategoryView implements MechanismView {

    constructor(private categoryMechanism:CategoryMechanism) {
        this.coordinateOwnInputAndRadioBoxes();
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
        return 'section#categoryMechanism' + this.categoryMechanism.id + '.category-type input';
    }

    coordinateOwnInputAndRadioBoxes() {
        // set constraints between radio boxes and own input fields
        if(this.categoryMechanism.active && this.categoryMechanism.getParameterValue('multiple') === 0 && this.categoryMechanism.getParameterValue('ownAllowed') === 1) {
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

    reset() {
        // TODO implement
    }
}