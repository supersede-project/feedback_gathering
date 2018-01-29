import React, { Component } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';

import 'react-tabs/style/react-tabs.css';

import '../../node_modules/react-accessible-accordion/dist/react-accessible-accordion.css';
import './App.css';
import MyFeedbacksTabAccordion from "./MyFeedbacksTabAccordion";
import { Widget, addResponseMessage } from 'react-chat-widget';
import FeedbackForumTabAccordion from "./FeedbackForumTabAccordion";
import CompanyViewAccordion from "./CompanyViewAccordion";

class App extends Component {

    componentWillMount() {
      if(sessionStorage.getItem('token') === null) {
        fetch(process.env.REACT_APP_BASE_URL + 'authenticate', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            name: 'admin',
            password: 'password'
          })
        }).then(result=>result.json())
        .then(result=> {
            sessionStorage.setItem('token', result.token);
            console.log(result);
          });
      }
      if(sessionStorage.getItem('userId') === null) {
        console.log("window userid" + window.userId);
        if(window.userId !== null) {
          sessionStorage.setItem('userId', window.userId);
        }
      }
      if(sessionStorage.getItem('applicationId') === null) {
        if(window.applicationId !== null) {
          sessionStorage.setItem('applicationId', window.applicationId);
        }
      }
    }

    componentDidMount() {
        addResponseMessage("Thank you for your time. We have some additional questions regarding your feedback.");
    }

    handleNewUserMessage(newMessage) {
        console.log(`New message incoming! ${newMessage}`);

    }

  render() {
    var toRender;

    if(window.adminUser) {
      toRender = <CompanyViewAccordion/>
    }
    else {
      toRender = <Tabs style={ {activeTabContentStyle: {backgroundColor:'#1A7E92', borderColor: '#1A7E92'}}}>
          <TabList>
              <Tab selectedClassName="tabselected">My Feedbacks</Tab>
              <Tab selectedClassName="tabselected">Feedback Forum</Tab>
          </TabList>

          <TabPanel>
              <MyFeedbacksTabAccordion/>
              <Widget handleNewUserMessage={this.handleNewUserMessage}/>
          </TabPanel>
          <TabPanel>
              <FeedbackForumTabAccordion/>
          </TabPanel>

      </Tabs>;
    }

    return (
      <div className="App">
        <header className="App-header">
        </header>
        {toRender}
      </div>
    );
  }
}

export default App;
