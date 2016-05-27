import * as gulp from 'gulp';
import {runSequence, task} from './tools/utils';


gulp.task('clean',       task('clean', 'all'));
gulp.task('clean.dist',  task('clean', 'dist'));
gulp.task('clean.test',  task('clean', 'test'));
gulp.task('clean.tmp',   task('clean', 'tmp'));


gulp.task('build.dev', done =>
    runSequence('clean.dist',
        'tslint',
        'build.sass.dev',
        'build.js.dev',
        done));