var gulp = require('gulp');
var config = require('./env/stage.json');
var SSH  = require('gulp-ssh');
var exec = require('child_process').exec;
var fs = require('fs');
var runSequence = require('run-sequence');


var gulpSSH = new SSH({
    ignoreErrors: false,
    sshConfig: {
        "host": config.host,
        "port": config.port,
        "username": config.username,
        "privateKey": fs.readFileSync(config.privateKeyPath)
    }
});

gulp.task('deploy', function (done) {
    runSequence(
        exec('webpack'),
        gulp.src(['index.html']).pipe(gulpSSH.dest(config.serverDest)),
        gulp.src(['dist/**']).pipe(gulpSSH.dest(config.serverDest + '/dist')),
        gulpSSH.shell(['sudo cp -R ' + config.serverDest + '/* ' + config.serverAppDir]),
        done
    );
});
