import i18n = require('i18next');
import {MechanismView} from '../mechanism_view';
import {dialogOptions} from '../../js/config';


/**
 * Acts as a wrapper to the jquery UI dialog
 */
export class DialogView {
    dialogElement:any;
    mechanismViews:MechanismView[];

    constructor(public dialogId:string, public template:any, public context?:any, public openCallback?:() => void,
                public closeCallback?:() => void) {
        let dialogContext = this.buildContext(context);
        let html = template(dialogContext);
        jQuery('body').append(html);
        this.initDialog();
    }

    initDialog() {
        var myThis = this,
            dialogContainer = jQuery('#'+ this.dialogId);

        this.dialogElement = dialogContainer.dialog(this.getDialogOptions());
        this.dialogElement.dialog('option', 'title', this.context.title);
        this.dialogElement.dialog('option', 'modal', this.context.modal);
        this.dialogElement.dialog('option', 'dialogClass', this.context.dialogCSSClass);

        dialogContainer.find('.discard-feedback').on('click', function () {
            myThis.discardFeedback();
        });
    }

    buildContext(applicationContext:any) {
        let dialogContext = {
            'dialogId': this.dialogId
        };
        return $.extend({}, applicationContext, dialogContext );
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
                this.dialog.dialog("close");
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
        for(var mechanismView of this.mechanismViews) {
            mechanismView.reset();
        }
    }

    discardFeedback() {
        this.resetDialog();
        this.close();
    }
}