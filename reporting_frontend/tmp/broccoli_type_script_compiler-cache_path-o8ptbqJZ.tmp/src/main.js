"use strict";
var platform_browser_dynamic_1 = require('@angular/platform-browser-dynamic');
var core_1 = require('@angular/core');
var _1 = require('./app/');
var http_1 = require('@angular/http');
var ServerConfiguration_1 = require('./app/services/ServerConfiguration');
if (_1.environment.production) {
    core_1.enableProdMode();
}
platform_browser_dynamic_1.bootstrap(_1.AppComponent, [http_1.HTTP_PROVIDERS, ServerConfiguration_1.ServerConfiguration]);
//# sourceMappingURL=main.js.map