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
        var resolution = window.screen.availHeight + 'x' + window.screen.availWidth;
        var userAgent = navigator.userAgent;

        var d = new Date();
        var localTime = new Date(d.getTime() - d.getTimezoneOffset() * 60000).toISOString();
        localTime = localTime.replace('T', ' ');
        localTime = localTime.replace('Z', ' ');
        localTime = localTime.slice(0, -3);
        var timeZone = d.toString().split("GMT")[1].split(" (")[0];

        var devicePixelRatio = ContextInformation.getDevicePixelRatio();
        var country = null;
        var region = null;
        var url = window.location.href;
        return new ContextInformation(resolution, userAgent, localTime, timeZone, devicePixelRatio, country, region, url, JSON.stringify(metaData));
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