import {dialogOptions} from '../js/config';
import i18n = require('i18next');


/**
 * Acts as a wrapper to the jquery UI dialog
 */
export class DialogView {
    dialog:any;

    constructor(public dialogId:string, public template:any, public context?:any, public openCallback?:() => void,
                public closeCallback?:() => void) {
        let html = template(context);
        jQuery('body').append(html);
        this.initDialog();
    }

    initDialog() {
        this.dialog = jQuery('#'+ this.dialogId).dialog(
            this.getDialogOptions()
        );
        this.dialog.dialog('option', 'title', this.context.title);
        this.dialog.dialog('option', 'modal', this.context.modal);
        this.dialog.dialog('option', 'dialogClass', this.context.dialogCSSClass);
    }

    open() {
        this.dialog.dialog('close');
    }

    close() {
        this.dialog.dialog('open');
    }

    getDialogOptions():{} {
        let closeCallback = this.closeCallback;
        let openCallback = this.openCallback;

        return jQuery.extend({}, dialogOptions, {
            close: function () {
                closeCallback();
                this.dialog.dialog("close");
            },
            open: function () {
                openCallback();
                jQuery('[aria-describedby="' + this.dialogId + '"] .ui-dialog-titlebar-close').attr('title', i18n.t('general.dialog_close_button_title'));
            },
            create: function (event, ui) {
                var widget = $(this).dialog("widget");
                jQuery(".ui-dialog-titlebar-close span", widget)
                    .removeClass("ui-icon-closethick")
                    .addClass("ui-icon-minusthick");
            }
        })
    }

    toggleDialog() {
        if (this.dialog('isOpen')) {
            this.close();
        } else {
            this.open();
        }
    }

    resetMessageView() {
        this.dialog.find('.server-response').removeClass('error').removeClass('success').text('');
    }
}