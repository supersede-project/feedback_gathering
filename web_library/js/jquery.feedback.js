;(function ($, window, document, undefined) {
    var dialog;

    initMechanisms = function (data) {
        var textConfig = data[0];

        $('h2#textTypeTitle').text(textConfig.parameters[0].value);
        $('span#textTypeMaxLength').text(textConfig.parameters[2].value);

        dialog.dialog("open");

        $('button#submitFeedback').on('click', function (event) {
            event.preventDefault();

            var text = $('textarea#textTypeText').val();
            var repositoryUrl = 'http://localhost:8080/feedback_repository/FeedbackServlet';
            $('#serverResponse').removeClass();

            $.ajax({
                url: repositoryUrl,
                type: 'POST',
                data: { text: text, component: "web library" },
                success: function (data) {
                    $('#serverResponse').addClass('success').text(data['success']);
                },
                error: function () {
                    $('#serverResponse').addClass('error').text('Failure: Server not responding');
                }
            });
        });
    };

    $.fn.feedback = function (options) {
        this.options = $.extend({}, $.fn.feedback.defaults, options);
        var currentOptions = this.options;
        var active = false;
        var dialogContainer = $('#feedbackContainer');

        this.css('background-color', currentOptions.backgroundColor);
        this.css('color', currentOptions.color);

        dialog = dialogContainer.dialog({
            autoOpen: false,
            height: 'auto',
            width: 'auto',
            minWidth: 500,
            modal: true,
            title: 'Feedback',
            buttons: {
            },
            close: function() {
                dialog.dialog("close");
                active = false;
            }
        });

        dialogContainer.find('.feedback-page').hide();
        dialogContainer.find('.feedback-page[data-feedback-page="1"]').show();

        dialogContainer.find('.feedback-dialog-forward').on('click', function() {
            var feedbackPage = $(this).closest('.feedback-page');
            var pageNumber = feedbackPage.data('feedback-page');
            var nextPageNumber = pageNumber + 1;

            feedbackPage.hide();
            var nextPage = $('.feedback-page[data-feedback-page="' + nextPageNumber + '"]');
            nextPage.show();

            if(nextPage.find('#textReview').length > 0) {
                nextPage.find('#textReview').text($('textarea#textTypeText').val());
            }
        });
        dialogContainer.find('.feedback-dialog-backward').on('click', function() {
            var feedbackPage = $(this).closest('.feedback-page');
            var pageNumber = feedbackPage.data('feedback-page');
            var nextPage = pageNumber - 1;

            feedbackPage.hide();
            $('.feedback-page[data-feedback-page="' + nextPage + '"]').show();
        });

        this.on('click', function () {
            if (!active) {
                $.get(currentOptions.backendUrl, null, function (data) {
                    initMechanisms(data);
                });
            } else {
                dialog.dialog("close");
            }
            active = !active;
        });
        return this;
    };

    $.fn.feedback.defaults = {
        'color': '#fff',
        'backgroundColor': '#a4e271',
        'backendUrl': 'http://ec2-54-175-37-30.compute-1.amazonaws.com/FeedbackConfiguration/text_rating.json',
        'postTestUrl': 'http://localhost:8080/feedback_repository/FeedbackServlet'
    };

})(jQuery, window, document);