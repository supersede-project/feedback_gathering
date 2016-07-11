let exec = require('child_process').exec;


export = function webpack() {
    return function () {
        exec('webpack');
    };
}
