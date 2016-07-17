import i18n = require('i18next');
import Handlebars = require('handlebars');


module.exports = function(i18n_key) {
    var result = i18n.t(i18n_key);
    return result;
};