define(["require", "exports", 'i18next'], function (require, exports, i18n) {
    "use strict";
    exports.validatePluginModule = (function ($, window, document) {
        $.fn.validate = function () {
            var content = this.val(), mandatory = this.data('mandatory'), defaultText = this.data('mandatory-default-text'), manualText = this.data('mandatory-manual-text'), maxLength = this.data('validation-max-length'), validMandatory = true, validMaxLength = true;
            var showValidationError = function (errorMessage, element) {
                element.after('<span class="feedback-form-error">' + errorMessage + '</span>');
            };
            this.next('.feedback-form-error').remove();
            this.removeClass('invalid');
            if (mandatory && content === '') {
                validMandatory = false;
            }
            if (maxLength && content.length > maxLength) {
                validMaxLength = false;
            }
            if (!validMandatory) {
                this.addClass('invalid');
                if (manualText === null || manualText === '') {
                    showValidationError(defaultText, this);
                }
                else {
                    showValidationError(manualText, this);
                }
                var invalidElement = this;
                $('html, body').animate({
                    scrollTop: invalidElement.offset().top
                }, 500);
            }
            if (!validMaxLength) {
                this.addClass('invalid');
                var errorMessageMaxLength = i18n.t('general.validation_max_length_error_message', {
                    maxLength: maxLength,
                    currentLength: content.length
                });
                showValidationError(errorMessageMaxLength, this);
                var invalidElement = this;
                $('html, body').animate({
                    scrollTop: invalidElement.offset().top
                }, 500);
            }
            return this;
        };
    });
    (function ($, window, document) {
        exports.validatePluginModule($, window, document);
    })(jQuery, window, document);
    requirejs.config({
        "shim": {
            "validate": ["jquery"]
        }
    });
});
//# sourceMappingURL=jquery.validate.js.map