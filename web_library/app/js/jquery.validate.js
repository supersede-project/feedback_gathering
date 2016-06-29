(function ($, window, document) {
    $.fn.validate = function () {
        var content = this.val(), mandatory = this.data('mandatory'), defaultText = this.data('mandatory-default-text'), manualText = this.data('mandatory-manual-text'), valid = true;
        $('.feedback-form-error').remove();
        this.removeClass('invalid');
        if (mandatory && content === '') {
            valid = false;
        }
        if (!valid) {
            this.addClass('invalid');
            if (manualText === null || manualText === '') {
                this.after('<span class="feedback-form-error">' + defaultText + '</span>');
            }
            else {
                this.after('<span class="feedback-form-error">' + manualText + '</span>');
            }
        }
        return this;
    };
})(jQuery, window, document);
