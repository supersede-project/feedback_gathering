import {join, normalize} from 'path';
import {argv} from 'yargs';


const ENVIRONMENTS = {
    DEVELOPMENT: 'dev',
    PRODUCTION: 'prod'
};

export const ENV                  = getEnvironment();

export const PROJECT_ROOT         = normalize(join(__dirname, '..'));
export const APP_SRC              = 'app';
export const TOOLS_DIR            = 'tools';
export const APP_DEST             = `dist`;
export const TEST_DEST            = `dist/test`;
export const TMP_DIR              = 'tmp';


function getEnvironment() {
    let base:string[] = argv['_'];
    let prodKeyword = !!base.filter(o => o.indexOf(ENVIRONMENTS.PRODUCTION) >= 0).pop();
    if (base && prodKeyword || argv['env'] === ENVIRONMENTS.PRODUCTION) {
        return ENVIRONMENTS.PRODUCTION;
    } else {
        return ENVIRONMENTS.DEVELOPMENT;
    }
}
