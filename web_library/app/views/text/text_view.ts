import {TextMechanism} from '../../models/mechanisms/text_mechanism';
import {TextFeedback} from '../../models/feedbacks/text_feedback';
import {MechanismView} from '../mechanism_view';


export class TextView implements MechanismView {

    constructor(private textMechanism:TextMechanism, private dialogId:string) {
        if (textMechanism.active) {
            this.initEvents();
        }
    }

    initEvents() {
        var container = jQuery('#' + this.dialogId);
        var sectionSelector = "textMechanism" + this.textMechanism.id;
        var textarea = container.find('section#' + sectionSelector + ' textarea.text-type-text');
        var maxLength = Math.trunc(this.textMechanism.getParameterValue('maxLength'));
        var isMaxLengthStrict = this.textMechanism.getParameterValue('maxLengthStrict');

        textarea.on('keyup focus paste blur', function () {
            container.find('section#' + sectionSelector + ' span.text-type-max-length').text($(this).val().length + '/' + maxLength);
        });

        if (isMaxLengthStrict) {
            // prevent typing if max length is reached
            textarea.on('keypress', function (e) {
                if (e.which < 0x20) {
                    // e.which < 0x20, then it's not a printable character
                    // e.which === 0 - Not a character
                    return;     // Do nothing
                }
                if (this.value.length === maxLength) {
                    e.preventDefault();
                } else if (this.value.length > maxLength) {
                    this.value = this.value.substring(0, maxLength);
                }
            });
            // prevent pasting more characters
            textarea.on('change blur', function () {
                if (this.value.length > maxLength) {
                    this.value = this.value.substring(0, maxLength - 1);
                }
            });
        }

        // text clear button
        container.find('section#' + sectionSelector + ' .text-type-text-clear').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            textarea.val('');
        });
    }

    getFeedback():TextFeedback {
        var text = jQuery('section#textMechanism' + this.textMechanism.id + ' textarea').val();
        return new TextFeedback(text, this.textMechanism.id)
    }

    reset() {
        var textarea = jQuery('#' + this.dialogId + ' #textMechanism' + this.textMechanism.id + ' textarea.text-type-text');
        textarea.val('');
    }
}