import React, { Component } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';

import 'react-tabs/style/react-tabs.css';

import '../../node_modules/react-accessible-accordion/dist/react-accessible-accordion.css';
import './App.css';
import MyFeedbacksTabAccordion from "./MyFeedbacksTabAccordion";
import { Widget, addResponseMessage } from 'react-chat-widget';
import FeedbackForumTabAccordion from "./FeedbackForumTabAccordion";

class App extends Component {

    componentDidMount() {
        addResponseMessage("Thank you for your time. We have some additional questions regarding your feedback.");
    }

    handleNewUserMessage(newMessage) {
        console.log(`New message incoming! ${newMessage}`);

    }

  render() {
    return (
      <div className="App">
        <header className="App-header">
        </header>


            <Tabs style={ {activeTabContentStyle: {backgroundColor:'#1A7E92', borderColor: '#1A7E92'}}}>
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

            </Tabs>
      </div>
    );
  }
}

export default App;
