define(["require", "exports"], function (require, exports) {
    "use strict";
    exports.validatePluginModule = (function ($, window, document) {
        $.fn.validate = function () {
            var content = this.val(), mandatory = this.data('mandatory'), defaultText = this.data('mandatory-default-text'), manualText = this.data('mandatory-manual-text'), maxLength = this.data('validation-max-length'), validMandatory = true, validMaxLength = true;
            $('.feedback-form-error').remove();
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
                    this.after('<span class="feedback-form-error">' + defaultText + '</span>');
                }
                else {
                    this.after('<span class="feedback-form-error">' + manualText + '</span>');
                }
                var invalidElement = this;
                $('html, body').animate({
                    scrollTop: invalidElement.offset().top
                }, 500);
            }
            if (!validMaxLength) {
                this.addClass('invalid');
                var errorMessageMaxLength = 'The maximum length of this field is ' + maxLength + '! You typed ' +
                    '' + content.length + ' characters.';
                this.after('<span class="feedback-form-error">' + errorMessageMaxLength + '</span>');
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