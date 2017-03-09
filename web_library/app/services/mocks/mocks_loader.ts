/**
 * Function to load the mocks JSON files during testing.
 *
 * @param url
 * @param base to prefix the url
 * @returns {null}
 */
export var readJSON = function (url, base:string = '') {
    url = base + url;

    var xhr = new XMLHttpRequest();
    var json = null;

    xhr.open("GET", url, false);

    xhr.onload = function (e) {
        if (xhr.status === 200) {
            json = JSON.parse(xhr.responseText);
        }

        else {
            console.error('readJSON', url, xhr.statusText);
            return null;
        }
    };

    xhr.onerror = function (e) {
        console.error('readJSON', url, xhr.statusText);
        return null;
    };

    xhr.send(null);
    return json;
};

export var readJSONAsync = function(url, callback:(data) => void, base:string = '') {
    jQuery.ajax({
        url: base + url,
        async: true,
        dataType: 'json',
        success: function (response) {
            callback(response);
        },
        error: function(error) {
            console.error('jQuery JSON load error: ' + error);
            callback(null)
        }
    });
};


