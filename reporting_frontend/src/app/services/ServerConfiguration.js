"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
/**
 * Created by flo on 14.07.16.
 */
var core_1 = require('@angular/core');
var ServerConfiguration = (function () {
    function ServerConfiguration() {
        this.Server = "http://ec2-54-175-37-30.compute-1.amazonaws.com/";
        this.ApiUrl = "feedback_repository/";
        this.ServerWithApiUrl = this.Server + this.ApiUrl;
    }
    ServerConfiguration = __decorate([
        core_1.Injectable()
    ], ServerConfiguration);
    return ServerConfiguration;
}());
exports.ServerConfiguration = ServerConfiguration;
//# sourceMappingURL=ServerConfiguration.js.map