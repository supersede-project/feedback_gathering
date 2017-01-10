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
            validMaxLength = true,
            validEmail = true,
            validationEmail = this.data('validation-email'),
            emailRegex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var showValidationError = function (errorMessage, element) {
            element.after('<span class="feedback-form-error">' + errorMessage + '</span>');
        };

        var isElementInViewport = function (el) {
            if (typeof jQuery === "function" && el instanceof jQuery) {
                el = el[0];
            }
            if(el === undefined) {
                return true;
            }

            var rect = el.getBoundingClientRect();

            return (
                rect.top >= 0 &&
                rect.left >= 0 &&
                rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && /*or $(window).height() */
                rect.right <= (window.innerWidth || document.documentElement.clientWidth) /*or $(window).width() */
            );
        };

        // reset
        this.next('.feedback-form-error').remove();
        this.removeClass('invalid');

        // validate mandatory
        if(mandatory && content === '') {
            validMandatory = false;
        }

        // validate max length
        if(maxLength && content.length > maxLength) {
            validMaxLength = false;
        }

        if(validationEmail && content !== '' && !emailRegex.test(content)) {
            validEmail = false;
        }

        // display messages
        if(!validMandatory) {
            this.addClass('invalid');
            if(manualText === null || manualText === '') {
                showValidationError(defaultText, this);
            } else {
                showValidationError(manualText, this);
            }

            // TODO refactor
            if(!isElementInViewport(invalidElement)) {
                var invalidElement = this;
                $('html, body').animate({
                    scrollTop: invalidElement.offset().top
                }, 500);
            }
        }

        if(!validMaxLength) {
            this.addClass('invalid');
            var errorMessageMaxLength = i18n.t('general.validation_max_length_error_message', {
                maxLength: maxLength,
                currentLength: content.length
            });
            showValidationError(errorMessageMaxLength, this);

            var invalidElement = this;
            if(!isElementInViewport(invalidElement)) {
                $('html, body').animate({
                    scrollTop: invalidElement.offset().top
                }, 500);
            }
        }

        if(!validEmail) {
            this.addClass('invalid');
            let errorMessage = i18n.t('general.validation_invalid_email');
            showValidationError(errorMessage, this);

            var invalidElement = this;

            if(!isElementInViewport(invalidElement)) {
                $('html, body').animate({
                    scrollTop: invalidElement.offset().top
                }, 500);
            }
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