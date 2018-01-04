import React, { Component } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import logo from './logo.svg';
import FaSlideshare from 'react-icons/lib/fa/slideshare';


class FeedbackCentral extends Component {
    render(){
        return (
            <div onClick="App"> <FaSlideshare Icon size={50}/> </div>
        );
    }
}

export default FeedbackCentral;