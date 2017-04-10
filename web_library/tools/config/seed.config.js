"use strict";
var path = require('path');

var SeedConfig = (function () {
    function SeedConfig() {
        this.APP_SRC = 'app';
        this.TOOLS_DIR = 'tools';
        this.DIST_DIR = 'dist';
        this.APP_DEST = this.DIST_DIR;
        this.PROJECT_TASKS_DIR = path.join(process.cwd(), this.TOOLS_DIR, 'tasks');
    }
    return SeedConfig;
}());
exports.SeedConfig = SeedConfig;