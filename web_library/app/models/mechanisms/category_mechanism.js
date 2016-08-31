var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './mechanism', '../feedbacks/category_feedback', '../feedbacks/category', '../feedbacks/category_type'], function (require, exports, mechanism_1, category_feedback_1, category_1, category_type_1) {
    "use strict";
    var CategoryMechanism = (function (_super) {
        __extends(CategoryMechanism, _super);
        function CategoryMechanism(id, type, active, order, canBeActivated, parameters) {
            _super.call(this, id, type, active, order, canBeActivated, parameters);
        }
        CategoryMechanism.prototype.getOptions = function () {
            return this.getParameterValue('options');
        };
        CategoryMechanism.prototype.getContext = function () {
            return {
                title: this.getParameterValue('title'),
                ownAllowed: this.getParameterValue('ownAllowed'),
                ownLabel: this.getParameterValue('ownLabel'),
                multiple: this.getParameterValue('multiple'),
                breakAfterOption: this.getParameterValue('breakAfterOption') ? true : false,
                options: this.getOptions(),
                inputType: this.getParameterValue('multiple') ? 'checkbox' : 'radio'
            };
        };
        CategoryMechanism.prototype.getCategoryFeedback = function () {
            var inputSelector = this.getInputSelector();
            var thisCategoryMechanism = this;
            var categories = [];
            jQuery(inputSelector).each(function () {
                var input = jQuery(this);
                if ((input.attr('type') === 'checkbox' || input.attr('type') === 'radio') && input.is(':checked')) {
                    var categoryKey = input.val();
                    var categoryValue = jQuery('section#categoryMechanism' + thisCategoryMechanism.id + '.category-type label[for="option' + categoryKey + '"]').text().trim();
                    var categoryType = new category_type_1.CategoryType(categoryKey, categoryValue);
                    var category = new category_1.Category(thisCategoryMechanism.id, null, null, categoryType);
                    categories.push(category);
                }
                else if (input.attr('type') === 'text' && input.val() !== "") {
                    var category = new category_1.Category(thisCategoryMechanism.id, input.val(), null, null);
                    categories.push(category);
                }
            });
            return new category_feedback_1.CategoryFeedback(this.id, categories);
        };
        CategoryMechanism.prototype.getInputSelector = function () {
            return 'section#categoryMechanism' + this.id + '.category-type input';
        };
        CategoryMechanism.prototype.coordinateOwnInputAndRadioBoxes = function () {
            if (this.active && this.getParameterValue('multiple') === 0 && this.getParameterValue('ownAllowed') === 1) {
                var ownTextInput = jQuery(this.getInputSelector() + '.own-category');
                var radioInputs = jQuery(this.getInputSelector() + '[type="radio"]');
                ownTextInput.on('keyup change', function () {
                    if (jQuery(this).val().length > 0) {
                        radioInputs.each(function () {
                            jQuery(this).prop('checked', false);
                        });
                    }
                });
                radioInputs.on('change', function () {
                    if (jQuery(this).is(':checked')) {
                        ownTextInput.val("");
                    }
                });
            }
        };
        return CategoryMechanism;
    }(mechanism_1.Mechanism));
    exports.CategoryMechanism = CategoryMechanism;
});
//# sourceMappingURL=category_mechanism.js.map