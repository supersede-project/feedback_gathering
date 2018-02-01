export class ContextInformation {
    resolution:string;
    userAgent:string;
    localTime:string;
    timeZone:string;
    devicePixelRatio:number;
    country:string;
    region:string;
    url:string;
    metaData:any;

    constructor(resolution:string, userAgent:string, localTime:string, timeZone:string, devicePixelRatio:number, country:string, region:string, url:string, metaData:any) {
        this.resolution = resolution;
        this.userAgent = userAgent;
        this.localTime = localTime;
        this.timeZone = timeZone;
        this.devicePixelRatio = devicePixelRatio;
        this.country = country;
        this.region = region;
        this.url = url;
        this.metaData = metaData;
    }

    static create(metaData) {
        let resolution = window.screen.availHeight + 'x' + window.screen.availWidth;
        let userAgent = navigator.userAgent;

        let d = new Date();
        let localTime = d.getTime() - d.getTimezoneOffset();
        let timeZone = d.toString().split("GMT")[1].split(" (")[0];

        let devicePixelRatio = ContextInformation.getDevicePixelRatio();
        let country = null;
        let region = null;
        let url = window.location.href;
        return new ContextInformation(resolution, userAgent, "" + localTime, timeZone, devicePixelRatio, country, region, url, JSON.stringify(metaData));
    }

    private static getDevicePixelRatio():number {
        var ratio = 1;
        // To account for zoom, change to use deviceXDPI instead of systemXDPI
        if (window.screen.systemXDPI !== undefined && window.screen.logicalXDPI !== undefined && window.screen.systemXDPI > window.screen.logicalXDPI) {
            // Only allow for values > 1
            ratio = window.screen.systemXDPI / window.screen.logicalXDPI;
        }
        else if (window.devicePixelRatio !== undefined) {
            ratio = window.devicePixelRatio;
        }
        return ratio;
    }
}