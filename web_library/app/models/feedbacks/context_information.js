define(["require", "exports"], function (require, exports) {
    "use strict";
    var ContextInformation = (function () {
        function ContextInformation(resolution, userAgent, localTime, timeZone, devicePixelRatio, country, region) {
            this.resolution = resolution;
            this.userAgent = userAgent;
            this.localTime = localTime;
            this.timeZone = timeZone;
            this.devicePixelRatio = devicePixelRatio;
            this.country = country;
            this.region = region;
        }
        ContextInformation.create = function () {
            var resolution = window.screen.availHeight + 'x' + window.screen.availWidth;
            var userAgent = navigator.userAgent;
            var d = new Date();
            var localTime = new Date(d.getTime() - d.getTimezoneOffset() * 60000).toISOString();
            localTime = localTime.replace('T', ' ');
            localTime = localTime.replace('Z', ' ');
            localTime = localTime.slice(0, -3);
            localTime = null;
            var timeZone = d.toString().split("GMT")[1].split(" (")[0];
            var devicePixelRatio = ContextInformation.getDevicePixelRatio();
            var country = null;
            var region = null;
            return new ContextInformation(resolution, userAgent, localTime, timeZone, devicePixelRatio, country, region);
        };
        ContextInformation.getDevicePixelRatio = function () {
            var ratio = 1;
            if (window.screen.systemXDPI !== undefined && window.screen.logicalXDPI !== undefined && window.screen.systemXDPI > window.screen.logicalXDPI) {
                ratio = window.screen.systemXDPI / window.screen.logicalXDPI;
            }
            else if (window.devicePixelRatio !== undefined) {
                ratio = window.devicePixelRatio;
            }
            return ratio;
        };
        return ContextInformation;
    }());
    exports.ContextInformation = ContextInformation;
});
//# sourceMappingURL=context_information.js.map