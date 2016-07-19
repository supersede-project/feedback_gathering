define(["require", "exports", 'querystring'], function (require, exports, querystring_1) {
    "use strict";
    var DataHelper = (function () {
        function DataHelper() {
        }
        DataHelper.dataURItoBlob = function (dataURI) {
            var byteString;
            if (dataURI.split(',')[0].indexOf('base64') >= 0)
                byteString = atob(dataURI.split(',')[1]);
            else
                byteString = querystring_1.unescape(dataURI.split(',')[1]);
            var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
            var ia = new Uint8Array(byteString.length);
            for (var i = 0; i < byteString.length; i++) {
                ia[i] = byteString.charCodeAt(i);
            }
            return new Blob([ia], { type: mimeString });
        };
        return DataHelper;
    }());
    exports.DataHelper = DataHelper;
});
//# sourceMappingURL=data_helper.js.map