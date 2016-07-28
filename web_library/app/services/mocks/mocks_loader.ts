/**
 * Function to load the mocks JSON files during testing.
 *
 * @param url
 * @returns {null}
 */
export var readJSON = function (url) {
    var base = '/base/';
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
        }
    };

    xhr.onerror = function (e) {
        console.error('readJSON', url, xhr.statusText);
    };

    xhr.send(null);
    return json;
};
