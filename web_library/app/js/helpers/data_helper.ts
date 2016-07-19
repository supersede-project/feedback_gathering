export class DataHelper {

    /**
     * Converts base64/URLEncoded data component to raw binary data.
     *
     * @param dataURI
     *  dataURI from a canvas element retrieved by canvas.toDataURL()
     * @returns {Blob}
     *  The image as a binary.
     */
    static dataURItoBlob(dataURI) {
        var byteString;
        if (dataURI.split(',')[0].indexOf('base64') >= 0)
            byteString = atob(dataURI.split(',')[1]);
        else
            byteString = decodeURIComponent(dataURI.split(',')[1]);

        // separate out the mime component
        var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

        // write the bytes of the string to a typed array
        var ia = new Uint8Array(byteString.length);
        for (var i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        return new Blob([ia], {type: mimeString});
    }
}