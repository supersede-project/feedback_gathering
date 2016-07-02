import * as gulp from 'gulp';
import {runSequence, task} from './tools/utils';
let exec = require('child_process').exec;


gulp.task('clean',       task('clean', 'all'));
gulp.task('clean.dist',  task('clean', 'dist'));
gulp.task('clean.test',  task('clean', 'test'));
gulp.task('clean.tmp',   task('clean', 'tmp'));




gulp.task('build', done =>
    runSequence(
        'tslint',
        'build.sass.dev',
        exec('webpack'),
        done));
