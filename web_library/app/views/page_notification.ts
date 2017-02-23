var notificationTemplate = require('../templates/notification.handlebars');


export class PageNotification {

    static show(message:string) {
        var html = notificationTemplate({message: message});
        $('html').append(html);
        setTimeout(function () {
            $(".feedback-notification").remove();
        }, 3000);
    }
}