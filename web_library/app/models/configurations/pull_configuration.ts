import {Mechanism} from '../mechanisms/mechanism';
import {Configuration} from './configuration';
import {GeneralConfiguration} from './general_configuration';
import {configurationTypes, cookieNames} from '../../js/config';


/**
 * Configuration object used for the pull feedback case. That means when there is a reminder, popup etc. that is
 * displayed to the user in order to get feedback from him.
 * Note that this class is extended by the Parameterizable mixin to provide methods on a parameter array field.
 */
export class PullConfiguration extends Configuration {

    constructor(id:number, mechanisms:Mechanism[], generalConfiguration:GeneralConfiguration) {
        super(id, mechanisms, configurationTypes.pull, generalConfiguration);
        this.dialogId = 'pullConfiguration';
    }

    /**
     * Decides whether the mechanisms associated with this configuration should get activated or not.
     *
     * @returns {boolean} true if the mechanismes should get triggered.
     */
    shouldGetTriggered():boolean {
        return this.isDoNotDisturbTimeDurationOver() && (this.generalConfiguration.getParameterValue('askOnAppStartup') ||
            Math.random() <= this.generalConfiguration.getParameterValue('likelihood'));
    }

    private isDoNotDisturbTimeDurationOver() {
        var doNotDisturbTimeDuration = 5 * 60;
        if (this.generalConfiguration.getParameterValue('doNotDisturbTimeDuration') != null) {
            doNotDisturbTimeDuration = this.generalConfiguration.getParameterValue('doNotDisturbTimeDuration');
        }
        return true;
        //return this.currentTimeStamp() - Number(this.getCookie(cookieNames.lastTriggered)) > doNotDisturbTimeDuration;
    }

    currentTimeStamp():number {
        if (!Date.now) {
            Date.now = function () {
                return new Date().getTime();
            }
        }
        return Math.floor(Date.now() / 1000);
    }

    wasTriggered():void {
        this.setCookie(cookieNames.lastTriggered, this.currentTimeStamp(), 365);
    }

    setCookie(cname, cvalue, exdays):void {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    }

    getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return null;
    }
}