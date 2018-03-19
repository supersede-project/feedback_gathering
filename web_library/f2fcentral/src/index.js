import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';

var f2fCentralOpened = false;

document.getElementById("openF2fcentral").onclick = function () {
    if (!f2fCentralOpened) {

        ReactDOM.render(<App/>, document.getElementById('f2fcentral'));
        document.getElementById('f2fcentral').style.display = 'block';
        registerServiceWorker();
        f2fCentralOpened = true;
    }
    else {
        ReactDOM.unmountComponentAtNode(document.getElementById('f2fcentral'));
        document.getElementById('f2fcentral').style.display = 'none';
        f2fCentralOpened = false;
    }
};

function fetchUnreadChat() {
    fetch(window.baseUrl + 'en/applications/' + window.applicationId + '/feedbacks/chat_unread/user/' + window.userId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(result => result.json())
        .then(result => {
            var element = document.getElementById("unreadFeedbacks")
            if (result != null && result.length > 0) {
                element.classList.remove("nodisplay");
            }
            else {
                if (!element.classList.contains("nodisplay")) {
                    element.classList.add("nodisplay");
                }
            }
        });
};

setInterval(fetchUnreadChat, 5000);
