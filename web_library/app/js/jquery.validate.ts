export interface JQuery {
    /**
     * Register a handler to validate a HTML element.
     *
     */
    validate():JQuery;
}

export var validatePlugin = (function($, window, document) {
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
            valid = true;

        // reset
        $('.feedback-form-error').remove();
        this.removeClass('invalid');

        if(mandatory && content === '') {
            valid = false;
        }
        if(!valid) {
            this.addClass('invalid');
            if(manualText === null || manualText === '') {
                this.after('<span class="feedback-form-error">' + defaultText + '</span>');
            } else {
                this.after('<span class="feedback-form-error">' + manualText + '</span>');
            }
        }
        return this;
    };
});

(function($, window, document) {
    validatePlugin($, window, document);
})(jQuery, window, document);

requirejs.config( {
    "shim": {
        "validate"  : ["jquery"]
    }
} );