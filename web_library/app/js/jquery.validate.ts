import i18n = require('i18next');


export interface JQuery {
    /**
     * Register a handler to validate a HTML element.
     *
     */
    validate():JQuery;
}

export var validatePluginModule = (function($, window, document) {
    /**
     * @returns {jQuery}
     *
     * The validate function implements the custom validation logic with the validation either taking place on the
     * final submit or on skip. Default or custom messages are supported as well.
     */
    $.fn.validate = function () {
        var content = this.val(),
            mandatory = this.data('mandatory'),
            defaultText = this.data('mandatory-default-text'),
            manualText = this.data('mandatory-manual-text'),
            maxLength = this.data('validation-max-length'),
            validMandatory = true,
            validMaxLength = true;

        // reset
        $('.feedback-form-error').remove();
        this.removeClass('invalid');

        // validate mandatory
        if(mandatory && content === '') {
            validMandatory = false;
        }

        // validate max length
        if(maxLength && content.length > maxLength) {
            validMaxLength = false;
        }

        // display messages
        if(!validMandatory) {
            this.addClass('invalid');
            if(manualText === null || manualText === '') {
                this.after('<span class="feedback-form-error">' + defaultText + '</span>');
            } else {
                this.after('<span class="feedback-form-error">' + manualText + '</span>');
            }

            var invalidElement = this;
            $('html, body').animate({
                scrollTop: invalidElement.offset().top
            }, 500);
        }
        if(!validMaxLength) {
            this.addClass('invalid');
            var errorMessageMaxLength = i18n.t('general.validation_max_length_error_message', {
                maxLength: maxLength,
                currentLength: content.length
            });
            
            this.after('<span class="feedback-form-error">' + errorMessageMaxLength + '</span>');

            var invalidElement = this;
            $('html, body').animate({
                scrollTop: invalidElement.offset().top
            }, 500);
        }
        return this;
    };
});

(function($, window, document) {
    validatePluginModule($, window, document);
})(jQuery, window, document);

requirejs.config( {
    "shim": {
        "validate"  : ["jquery"]
    }
} );