import i18n = require('i18next');
import {dialogOptions} from '../../js/config';


/**
 * Acts as a wrapper to the jquery UI dialog
 */
export class DialogView {
    dialogElement:any;
    dialogContext:any;

    constructor(public dialogId:string, public template:any, public context:any, public openCallback?:() => void,
                public closeCallback?:() => void) {
        this.dialogContext = this.buildContext(context);
        let html = template(this.dialogContext);
        jQuery('body').append(html);
        this.initDialog();
    }

    initDialog() {
        let dialogContainer = jQuery('#'+ this.dialogId);

        this.dialogElement = dialogContainer.dialog(this.getDialogOptions());
        this.dialogElement.dialog('option', 'title', this.dialogContext.dialogTitle);
        this.dialogElement.dialog('option', 'modal', this.dialogContext.modal);
        this.dialogElement.dialog('option', 'dialogClass', this.dialogContext.dialogCSSClass);

        this.setupCloseOnOutsideClick();
    }

    buildContext(applicationContext:any) {
        let dialogContext = {
            'dialogId': this.dialogId
        };
        this.context = $.extend({}, applicationContext, dialogContext);
        return this.context;
    }

    setupCloseOnOutsideClick() {
        let clickBlocked = false;
        var dialogView = this;

        // prevent dialog close on drag stop click
        jQuery('body').on('dragstart', function () {
            clickBlocked = true;
        }).on('dragstop', function () {
            setTimeout(function () {
                clickBlocked = false;
            }, 100);
        }).on('click', function (event) {
            if (!clickBlocked && dialogView.dialogElement.dialog('isOpen') && !jQuery(event.target).is('.ui-dialog, a') && !jQuery(event.target).closest('.ui-dialog').length) {
                dialogView.dialogElement.dialog('close');
            }
        });
    }

    open() {
        this.dialogElement.dialog('open');
    }

    close() {
        this.dialogElement.dialog('close');
    }

    getDialogOptions():{} {
        let closeCallback = this.closeCallback;
        let openCallback = this.openCallback;

        return jQuery.extend({}, dialogOptions, {
            close: function () {
                if(closeCallback) {
                    closeCallback();
                }
            },
            open: function () {
                if(openCallback) {
                    openCallback();
                }
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
        if(this.dialogElement.dialog('isOpen')) {
            this.close();
        } else {
            this.open();
        }
    }

    resetMessageView() {
        this.dialogElement.find('.server-response').removeClass('error').removeClass('success').text('');
    }

    resetDialog() {

    }
}