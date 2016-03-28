;(function($, window, document, undefined) {

    $.fn.feedback = function(options) {
        this.options = $.extend({}, $.fn.feedback.defaults, options);
        var currentOptions = this.options;

        this.css('background-color', currentOptions.backgroundColor);
        this.css('color', currentOptions.color);

        this.on('click', function() {
            $.get(currentOptions.backendUrl, null, function(data) {
                alert('data' + JSON.stringify(data));
            })
        });
        return this;
    };

    $.fn.feedback.defaults = {
        'color': '#fff',
        'backgroundColor': '#ff0000',
        'backendUrl': 'http://ec2-54-175-37-30.compute-1.amazonaws.com/feedback_repository/FeedbackServlet'
    };

})(jQuery, window, document);