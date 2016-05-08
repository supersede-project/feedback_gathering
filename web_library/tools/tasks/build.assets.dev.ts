import {join} from 'path';
import {APP_SRC, APP_DEST} from '../config';
var gulpIgnore = require('gulp-ignore');
var condition = '_*.scss';

export = function buildAssetsDev(gulp, plugins) {
  return function () {
    return gulp.src([
        join(APP_SRC, '**'),
        '!' + join(APP_SRC, '**', '*.ts')
      ])
      .pipe(gulpIgnore.exclude(condition))
      .pipe(gulp.dest(APP_DEST));
  };
}
