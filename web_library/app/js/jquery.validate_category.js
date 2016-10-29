define(["require", "exports"], function (require, exports) {
    "use strict";
    exports.validateCategoryPluginModule = (function ($, window, document) {
        $.fn.validateCategory = function () {
            var mandatory = this.data('mandatory'), manualText = this.data('mandatory-manual-text'), validMandatory = true;
            var showValidationError = function (errorMessage, element) {
                element.append('<span class="feedback-form-error">' + errorMessage + '</span>');
            };
            this.find('.feedback-form-error').remove();
            this.removeClass('invalid');
            if (mandatory && this.find('input:checked').length === 0) {
                validMandatory = false;
            }
            if (!validMandatory) {
                this.addClass('invalid');
                showValidationError(manualText, this);
                var invalidElement = this;
                $('html, body').animate({
                    scrollTop: invalidElement.offset().top
                }, 500);
            }
            return this;
        };
    });
    (function ($, window, document) {
        exports.validateCategoryPluginModule($, window, document);
    })(jQuery, window, document);
    requirejs.config({
        "shim": {
            "validateCategory": ["jquery"]
        }
    });
});
//# sourceMappingURL=jquery.validate_category.js.map