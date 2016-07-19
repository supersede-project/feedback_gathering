"use strict";
var platform_browser_dynamic_1 = require('@angular/platform-browser-dynamic');
var core_1 = require('@angular/core');
var _1 = require('./app/');
var http_1 = require('@angular/http');
var ServerConfiguration_1 = require('./app/services/ServerConfiguration');
var forms_1 = require('@angular/forms');
if (_1.environment.production) {
    core_1.enableProdMode();
}
platform_browser_dynamic_1.bootstrap(_1.AppComponent, [http_1.HTTP_PROVIDERS, ServerConfiguration_1.ServerConfiguration, forms_1.disableDeprecatedForms(), forms_1.provideForms()]);
//# sourceMappingURL=main.js.map