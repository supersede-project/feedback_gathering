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
        return ContextInformation;
    }());
    exports.ContextInformation = ContextInformation;
});
//# sourceMappingURL=context_information.js.map