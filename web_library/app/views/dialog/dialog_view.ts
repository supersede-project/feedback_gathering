import {dialogOptions} from '../js/config';
import i18n = require('i18next');
import {MechanismView} from '../mechanism_view';


/**
 * Acts as a wrapper to the jquery UI dialog
 */
export class DialogView {
    dialog:any;
    mechanismViews:MechanismView[];

    constructor(public dialogId:string, public template:any, public context?:any, public openCallback?:() => void,
                public closeCallback?:() => void) {
        let html = template(context);
        jQuery('body').append(html);
        this.initDialog();
    }

    initDialog() {
        var myThis = this,
            dialogContainer = jQuery('#'+ this.dialogId);

        this.dialog = dialogContainer.dialog(this.getDialogOptions());
        this.dialog.dialog('option', 'title', this.context.title);
        this.dialog.dialog('option', 'modal', this.context.modal);
        this.dialog.dialog('option', 'dialogClass', this.context.dialogCSSClass);

        dialogContainer.find('.discard-feedback').on('click', function () {
            myThis.discardFeedback();
        });
    }

    open() {
        this.dialog.dialog('opem');
    }

    close() {
        this.dialog.dialog('close');
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
        if(this.dialog('isOpen')) {
            this.close();
        } else {
            this.open();
        }
    }

    resetMessageView() {
        this.dialog.find('.server-response').removeClass('error').removeClass('success').text('');
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