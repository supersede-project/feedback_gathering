var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
define(["require", "exports", './configuration', '../../js/config'], function (require, exports, configuration_1, config_1) {
    "use strict";
    var PullConfiguration = (function (_super) {
        __extends(PullConfiguration, _super);
        function PullConfiguration(id, mechanisms, generalConfiguration) {
            _super.call(this, id, mechanisms, config_1.configurationTypes.pull, generalConfiguration);
            this.dialogId = 'pullConfiguration';
        }
        PullConfiguration.prototype.shouldGetTriggered = function () {
            return this.pageDoesMatch(this.currentSlug()) && this.isDoNotDisturbTimeDurationOver() && (this.generalConfiguration.getParameterValue('askOnAppStartup') ||
                Math.random() <= this.generalConfiguration.getParameterValue('likelihood'));
        };
        PullConfiguration.prototype.isDoNotDisturbTimeDurationOver = function () {
            var doNotDisturbTimeDuration = 5 * 60;
            if (this.generalConfiguration.getParameterValue('doNotDisturbTimeDuration') != null) {
                doNotDisturbTimeDuration = this.generalConfiguration.getParameterValue('doNotDisturbTimeDuration');
            }
            return this.currentTimeStamp() - Number(this.getCookie(config_1.cookieNames.lastTriggered)) > doNotDisturbTimeDuration;
        };
        PullConfiguration.prototype.currentTimeStamp = function () {
            if (!Date.now) {
                Date.now = function () {
                    return new Date().getTime();
                };
            }
            return Math.floor(Date.now() / 1000);
        };
        PullConfiguration.prototype.wasTriggered = function () {
            this.setCookie(config_1.cookieNames.lastTriggered, this.currentTimeStamp(), 365);
        };
        PullConfiguration.prototype.currentSlug = function () {
            var url = location.href;
            return url.replace(/http(s*):\/\/[^\/]*\//i, "");
        };
        PullConfiguration.prototype.pageDoesMatch = function (slug) {
            var pages = this.generalConfiguration.getParameterValue('pages');
            if (pages === null || pages.length === 0) {
                return true;
            }
            else {
                for (var _i = 0, pages_1 = pages; _i < pages_1.length; _i++) {
                    var page = pages_1[_i];
                    if (page.value === slug) {
                        return true;
                    }
                }
                return false;
            }
        };
        PullConfiguration.prototype.setCookie = function (cname, cvalue, exdays) {
            var d = new Date();
            d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
            var expires = "expires=" + d.toUTCString();
            document.cookie = cname + "=" + cvalue + "; " + expires;
        };
        PullConfiguration.prototype.getCookie = function (cname) {
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
        };
        return PullConfiguration;
    }(configuration_1.Configuration));
    exports.PullConfiguration = PullConfiguration;
});
//# sourceMappingURL=pull_configuration.js.map