export class ContextInformation {
    resolution:string;
    userAgent:string;
    localTime:string;
    timeZone:string;
    devicePixelRatio:number;
    country:string;
    region:string;

    constructor(resolution:string, userAgent:string, localTime:string, timeZone:string, devicePixelRatio:number, country:string, region:string) {
        this.resolution = resolution;
        this.userAgent = userAgent;
        this.localTime = localTime;
        this.timeZone = timeZone;
        this.devicePixelRatio = devicePixelRatio;
        this.country = country;
        this.region = region;
    }
}