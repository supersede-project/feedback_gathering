var gulp = require('gulp');
var config = require('./env/stage/stage.json');
var SSH  = require('gulp-ssh');
var exec = require('child_process').exec;
var fs = require('fs');
var insert = require('gulp-insert');
var cleanCSS = require('gulp-clean-css');
var concatCss = require('gulp-concat-css');
var replace = require('gulp-replace');
var rename = require('gulp-rename');
var runSequence = require('run-sequence').use(gulp);
var del = require('del');
var uglify = require('gulp-uglify');
var argv = require('yargs').argv;
var ts = require("gulp-typescript");
var tsProject = ts.createProject("tsconfig.json");
var sass = require('gulp-sass');
var project_utils = require('./tools/utils');
var project_config = require('./tools/config');

project_utils.loadTasks(project_config.PROJECT_TASKS_DIR);


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

var version = function () {
    var versionString = fs.readFileSync('version.txt');
    return '/*' + versionString + '*/';
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
        .pipe(insert.prepend(copyright()))
        .pipe(gulp.dest('dist/'));
});

gulp.task('add-version', function () {
    gulp.src(['dist/jquery.feedback.min.js'])
        .pipe(insert.prepend(version()))
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

gulp.task('concat-copy-all-css', function () {
    return gulp.src([jqueryUIPath + 'jquery-ui.css', 'app/css/main.css'])
        .pipe(replace('url("images/', 'url("jqueryui/images/'))
        .pipe(concatCss("main.min.css"))
        .pipe(gulp.dest('dist'));
});

gulp.task('concat-minify-copy-all-css', function () {
    return gulp.src([jqueryUIPath + 'jquery-ui.css', 'app/css/main.css'])
        .pipe(replace('url("images/', 'url("jqueryui/images/'))
        .pipe(concatCss("main.min.css"))
        .pipe(cleanCSS())
        .pipe(gulp.dest('dist'));
});

gulp.task('copy-jquery-ui-droppable', function () {
    gulp.src(['app/js/lib/jquery.ui.droppable.js'])
        .pipe(gulp.dest('dist'));
});

gulp.task('build-jquery-ui', function() {
    gulp.src([jqueryUIPath + 'jquery-ui.min.js', jqueryUIPath + 'LICENSE.txt'])
        .pipe(gulp.dest('dist/jqueryui/'));
    /*
    gulp.src([jqueryUIPath + 'jquery-ui.css'])
        .pipe(cleanCSS())
        .pipe(rename('jquery-ui.min.css'))
        .pipe(gulp.dest('dist/jqueryui'));
        */
    gulp.src([jqueryUIPath + 'images/*'])
        .pipe(gulp.dest('dist/jqueryui/images'));
});

gulp.task('copy-test-page-assets', function() {
    gulp.src(['env/dev/test_page_assets/**'])
        .pipe(gulp.dest('dist'));
    gulp.src(['env/dev/Energiesparkonto_ Energie sparen. Kosten senken._files/**'])
        .pipe(gulp.dest('dist/Energiesparkonto_ Energie sparen. Kosten senken._files'));
    gulp.src(['env/dev/digitale_doerfer_files/**'])
        .pipe(gulp.dest('dist/digitale_doerfer_files'));
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
    gulp.src(['app/js/lib/screenshot/fabric.min.js', 'app/js/lib/screenshot/customiseControls.js', 'app/js/lib/screenshot/spectrum.js', 'app/js/lib/screenshot/html2canvas_5_0_4.min.js', 'app/js/lib/screenshot/html2canvas_5_0_4.svg.min.js'])
        .pipe(gulp.dest('dist/screenshot'));
});

gulp.task('copy-and-uglify-screenshot-assets', function() {
    gulp.src(['app/js/lib/screenshot/fabric.min.js', 'app/js/lib/screenshot/customiseControls.js', 'app/js/lib/screenshot/spectrum.js', 'app/js/lib/screenshot/html2canvas_5_0_4.min.js', 'app/js/lib/screenshot/html2canvas_5_0_4.svg.min.js'])
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
gulp.task('configure.config.ts', function() {
    var configuration = argv.configuration || 'default';

    // js/config.* = js/configurations/common.* + js/configurations/<configuration>.*
    gulp.src(['app/js/configurations/common.ts'])
        .pipe(rename('config.ts'))
        .pipe(insert.prepend('// Do not edit this file but the files in the configurations/ folder\n\n'))
        .pipe(insert.append('\n' + getFileContent('app/js/configurations/' + configuration + '.ts')))
        .pipe(gulp.dest('app/js'))
        .pipe(tsProject())
        .js.pipe(gulp.dest('app/js'));
});

gulp.task('set.config.scss', function() {
    var base_configuration = argv.configuration || 'default';
    var configuration = argv.style_configuration || base_configuration;

    gulp.src(['app/css/configurations/_' + configuration + '.scss'])
        .pipe(insert.prepend('// Do not edit this file but the files in the configurations/ folder\n\n'))
        .pipe(rename('_config.scss'))
        .pipe(gulp.dest('app/css'));
});

// builds main.css
gulp.task('build.css', function () {
    return gulp.src('app/css/main.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest('app/css'));
});

/**
 * Build the main.css according to the passed parameter
 */
gulp.task('configure.config.scss', function(done) {
    runSequence(
        'set.config.scss',
        'build.css',
        done
    );
});

/**
 * Arguments:
 *  - configuration: configuration for typescript
 *  - style_configuration: determines scss configuration (optional, default: configuration)
 */
gulp.task('configure', function(done) {
    runSequence(
        'configure.config.scss',
        'configure.config.ts',
        done
    );
});

gulp.task('build.dev', function(done) {
    runSequence(
        'clean',
        'build.js.dev',
        'configure',
        'webpack.dev',
        'copy-jquery',
        'concat-copy-all-css',
        //'copy-css',
        'build-jquery-ui',
        'copy-screenshot-assets',
        'copy-audio-assets',
        'copy-test-page-assets',
        'copy-images',
        'copy-locales',
        'copy-jquery-ui-droppable',
        'add-copyright',
        'add-version',
        done
    );
});

gulp.task('build.prod', function(done) {
    runSequence(
        'clean',
        'build.js.dev',
        'configure',
        'webpack.prod',
        'copy-jquery',
        'concat-minify-copy-all-css',
        //'copy-and-minify-css',
        'build-jquery-ui',
        'copy-and-uglify-screenshot-assets',
        'copy-and-uglify-audio-assets',
        'copy-images',
        'copy-locales',
        'copy-jquery-ui-droppable',
        'add-copyright',
        'add-version',
        done
    );
});