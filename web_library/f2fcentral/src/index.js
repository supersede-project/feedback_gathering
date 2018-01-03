import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';

var f2fCentralOpened = false;

document.getElementById("openF2fcentral").onclick = function() {
    if(!f2fCentralOpened) {

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
