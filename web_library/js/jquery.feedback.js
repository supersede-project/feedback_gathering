;(function ($, window, document, undefined) {

    initMechanisms = function (data) {
        var textConfig = data[0];

        $('h1#textTypeTitle').text(textConfig.parameters[0].value);
        $('span#textTypeMaxLength').text(textConfig.parameters[2].value);

        $('#feedbackContainer').show();

        $('form').on('submit', function (event) {
            event.preventDefault();

            var text = $('textarea#textTypeText').val();
            var repositoryUrl = 'http://localhost:8080/feedback_repository/FeedbackServlet';

            $.ajax({
                method: "POST",
                url: repositoryUrl,
                data: { text: text, component: "web library" }
            })
            .done(function( msg ) {

            });
        });
    };

    $.fn.feedback = function (options) {
        this.options = $.extend({}, $.fn.feedback.defaults, options);
        var currentOptions = this.options;
        var active = false;

        this.css('background-color', currentOptions.backgroundColor);
        this.css('color', currentOptions.color);

        this.on('click', function () {
            if (!active) {
                $.get(currentOptions.backendUrl, null, function (data) {
                    initMechanisms(data);
                });
            } else {
                $('#feedbackContainer').hide();
            }
            active = !active;
        });
        return this;
    };

    $.fn.feedback.defaults = {
        'color': '#fff',
        'backgroundColor': '#ffff00',
        'backendUrl': 'http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_rating.json',
        'postTestUrl': 'http://localhost:8080/feedback_repository/FeedbackServlet'
    };

})(jQuery, window, document);