import i18n = require('i18next');
import {dialogOptions} from '../../js/config';

export declare var clickBlocked:boolean;

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

        this.dialogElement.parent('.ui-dialog').wrapAll('<div class="feedback-library-wrapper"></div>');
        this.dialogElement.parent('.ui-dialog').closest('.ui-widget-overlay').wrapAll('<div class="feedback-library-wrapper"></div>');

        this.dialogElement.dialog('option', 'modal', this.dialogContext.modal);
        this.dialogElement.dialog('option', 'dialogClass', this.dialogContext.dialogCSSClass);

        if(this.context.localesOverride && this.context.localesOverride.dialog && this.context.localesOverride.dialog.dialog && this.context.localesOverride.dialog.dialog.titles) {
            if(this.context.localesOverride.dialog.dialog.titles["1"]) {
                this.dialogElement.dialog('option', 'title', this.context.localesOverride.dialog.dialog.titles["1"]);
            } else {
                this.dialogElement.dialog('option', 'title', this.dialogContext.dialogTitle);
            }
        }

        if(this.context.closeOnOutsideClick === null || this.context.closeOnOutsideClick === undefined || this.context.closeOnOutsideClick === true) {
            this.setupCloseOnOutsideClick();
        }
    }

    overrideMechanismConfiguration(context:any, localesOverride:any): any {
        if(localesOverride.mechanisms && context.mechanisms) {
            let mechanismsOverride = localesOverride.mechanisms;

            for(let mechanismOverride of mechanismsOverride) {
                if(context.mechanisms.filter(mechanism => mechanism.id === mechanismOverride.id).length > 0) {
                    let foundMechanism = context.mechanisms.filter(mechanism => mechanism.id === mechanismOverride.id)[0];
                    let index = context.mechanisms.indexOf(foundMechanism);
                    context.mechanisms[index] = jQuery.extend(true, context.mechanisms[index], mechanismOverride);
                }
            }
        }
        return context;
    }

    buildContext(applicationContext:any) {
        let dialogContext = {
            'dialogId': this.dialogId
        };
        this.context = $.extend({}, applicationContext, dialogContext);
        if(this.context.localesOverride) {
            this.context = this.overrideMechanismConfiguration(this.context, this.context.localesOverride);
        }
        return this.context;
    }

    setupCloseOnOutsideClick() {
        clickBlocked = false;
        let dialogView = this;

        // prevent dialog close on drag stop click
        jQuery('.ui-dialog').on('dragstart', function () {
            clickBlocked = true;
        });

        jQuery('body').on('click', function (event) {
            if (!clickBlocked && dialogView.dialogElement.dialog('isOpen') && !jQuery(event.target).is('.ui-dialog, a') && !jQuery(event.target).closest('.ui-dialog').length) {
                dialogView.dialogElement.dialog('close');
            }

            if(clickBlocked) {
                clickBlocked = false;
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
        let myThis = this;

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
                jQuery('[aria-describedby="' + myThis.dialogId + '"] .ui-dialog-titlebar-close').attr('title', i18n.t('general.dialog_close_button_title'));
                jQuery('.ui-widget-overlay').attr('style', 'height: 0 !important;');
                if(myThis.dialogId === 'pullConfiguration') {
                    jQuery('[aria-describedby="' + myThis.dialogId + '"] .ui-dialog-titlebar-close').hide();
                }
            },
            create: function (event, ui) {
                let widget = $(this).dialog("widget");
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

    setTitle(title:string) {
        this.dialogElement.dialog('option', 'title', title);
    }

    setModal(modal:boolean) {
        this.dialogElement.dialog('option', 'modal', modal);
    }
}