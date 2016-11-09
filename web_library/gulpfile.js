var gulp = require('gulp');
var config = require('./env/stage/stage.json');
var SSH  = require('gulp-ssh');
var exec = require('child_process').exec;
var fs = require('fs');
var insert = require('gulp-insert');
var cleanCSS = require('gulp-clean-css');
var rename = require('gulp-rename');
var runSequence = require('run-sequence').use(gulp);
var del = require('del');
var uglify = require('gulp-uglify');
var argv = require('yargs').argv;
var ts = require("gulp-typescript");
var tsProject = ts.createProject("tsconfig.json");


var jqueryUIPath = 'app/assets/jquery-ui-1.12.1.custom/';

var gulpSSH = new SSH({
    ignoreErrors: false,
    sshConfig: {
        "host": config.host,
        "port": config.port,
        "username": config.username,
        "privateKey": fs.readFileSync(config.privateKeyPath)
    }
});

var copyright = function () {
    var copyrightString = fs.readFileSync('copyright.txt');
    return '/*' + copyrightString + '*/';
};

var getFileContent = function (path) {
    return fs.readFileSync(path);
};

gulp.task('webpack.dev', function() {
    exec('webpack');
});

gulp.task('webpack.prod', function() {
    exec('webpack -p');
});

gulp.task('deploy', function (done) {
    runSequence(
        gulp.src(['index.html']).pipe(gulpSSH.dest(config.serverDest)),
        gulp.src(['dist/**']).pipe(gulpSSH.dest(config.serverDest + '/dist')),
        gulpSSH.shell(['sudo cp -R ' + config.serverDest + '/* ' + config.serverAppDir]),
        done
    );
});

gulp.task('add-copyright', function () {
    gulp.src(['dist/jquery.feedback.min.js'])
    .pipe(insert.prepend(copyright))
    .pipe(gulp.dest('dist/'));
});

gulp.task('copy-and-minify-css', function () {
    gulp.src(['app/css/main.css'])
        .pipe(cleanCSS())
        .pipe(rename('main.min.css'))
        .pipe(gulp.dest('dist'));
});

gulp.task('copy-css', function () {
    gulp.src(['app/css/main.css'])
        .pipe(rename('main.min.css'))
        .pipe(gulp.dest('dist'));
});

gulp.task('build-jquery-ui', function() {
    gulp.src([jqueryUIPath + 'jquery-ui.min.js', jqueryUIPath + 'jquery-ui.min.css', jqueryUIPath + 'LICENSE.txt'])
        .pipe(gulp.dest('dist/jqueryui/'));
    gulp.src([jqueryUIPath + 'images/*'])
        .pipe(gulp.dest('dist/jqueryui/images'));
});

gulp.task('copy-test-page-assets', function() {
    gulp.src(['env/dev/test_page_assets/**'])
        .pipe(gulp.dest('dist'));
});

gulp.task('clean', function() {
    return del([
        'dist/**'
    ]);
});

gulp.task('copy-jquery', function() {
    gulp.src(['app/js/lib/jquery-1.9.1.js'])
        .pipe(gulp.dest('dist'));
});

gulp.task('copy-locales', function() {
    gulp.src(['app/locales/**'])
        .pipe(gulp.dest('dist/locales'));
});

gulp.task('copy-images', function() {
    gulp.src(['app/img/**'])
        .pipe(gulp.dest('dist/img'));
});

gulp.task('copy-screenshot-assets', function() {
    gulp.src(['app/js/lib/screenshot/fabric.min.js', 'app/js/lib/screenshot/customiseControls.js', 'app/js/lib/screenshot/spectrum.js'])
        .pipe(gulp.dest('dist/screenshot'));
});

gulp.task('copy-and-uglify-screenshot-assets', function() {
    gulp.src(['app/js/lib/screenshot/fabric.min.js', 'app/js/lib/screenshot/customiseControls.js', 'app/js/lib/screenshot/spectrum.js'])
        .pipe(uglify({mangle:false}))
        .pipe(gulp.dest('dist/screenshot'));
});

gulp.task('copy-audio-assets', function() {
    gulp.src(['app/js/lib/audio/Fr.voice.js', 'app/js/lib/audio/recorder.js'])
        .pipe(gulp.dest('dist/audio'));
});

gulp.task('copy-and-uglify-audio-assets', function() {
    gulp.src(['app/js/lib/audio/Fr.voice.js', 'app/js/lib/audio/recorder.js'])
        .pipe(uglify({mangle:false}))
        .pipe(gulp.dest('dist/audio'));
});

/**
 * Builds the configuration according to the passed parameter and also compiles the config js file.
 */
gulp.task('configure', function() {
    var configuration = argv.configuration || 'default';

    // js/config.* = js/configurations/common.* + js/configurations/<configuration>.*
    gulp.src(['app/js/configurations/common.ts'])
        .pipe(rename('config.ts'))
        .pipe(insert.append('\n' + getFileContent('app/js/configurations/' + configuration + '.ts')))
        .pipe(gulp.dest('app/js'))
        .pipe(ts(tsProject))
        .js.pipe(gulp.dest('app/js'));
});

gulp.task('build.dev', function(done) {
    runSequence(
        'clean',
        'webpack.dev',
        'copy-jquery',
        'copy-css',
        'build-jquery-ui',
        'copy-screenshot-assets',
        'copy-audio-assets',
        'copy-test-page-assets',
        'copy-images',
        'copy-locales',
        done
    );
});

gulp.task('build.prod', function(done) {
    runSequence(
        'clean',
        'webpack.prod',
        'copy-jquery',
        'copy-and-minify-css',
        'build-jquery-ui',
        'copy-and-uglify-screenshot-assets',
        'copy-and-uglify-audio-assets',
        'copy-images',
        'copy-locales',
        done
    );
});