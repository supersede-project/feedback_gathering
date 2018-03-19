import React, {Component} from 'react';
import {Tab, Tabs, TabList, TabPanel} from 'react-tabs';

import 'react-tabs/style/react-tabs.css';
import style from 'react-tabs/style/react-tabs.css';

import '../../node_modules/react-accessible-accordion/dist/react-accessible-accordion.css';
import './App.css';
import MyFeedbacksTabAccordion from "./MyFeedbacksTabAccordion";
import FeedbackForumTabAccordion from "./FeedbackForumTabAccordion";
import CompanyViewAccordion from "./CompanyViewAccordion";
import LoginAdmin from "./LoginAdmin";
import CompanyFeedbackListViewAccordion from "./CompanyFeedbackListViewAccordion";


class App extends Component {

  constructor(props) {
    super(props);
    this.state = [{loggedIn : false}];
    this.adminLoggedIn = this.adminLoggedIn.bind(this);
  }

  componentWillMount() {
    var that = this;
    if (sessionStorage.getItem('token') === null) {
      fetch(process.env.REACT_APP_BASE_URL + 'authenticate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          name: 'admin',
          password: 'password'
        })
      }).then(result => result.json())
      .then(result => {
        sessionStorage.setItem('token', result.token);
      });
    }
    if (sessionStorage.getItem('userId') === null) {
    }

      if (window.userId !== null) {
          sessionStorage.setItem('userId', window.userId);
      }

      if (sessionStorage.getItem('applicationId') === null) {
      if (window.applicationId !== null) {
        sessionStorage.setItem('applicationId', window.applicationId);
      }
    }

  }

  adminLoggedIn(token) {
    sessionStorage.setItem('token', token);
    this.setState({loggedIn : true});
  }


  render() {
    var toRender;

    if (window.adminUser) {

      if (!this.state.loggedIn || sessionStorage.getItem('token') === null) {
        toRender = <LoginAdmin loginSuccess={this.adminLoggedIn}/>
      }
      else {
        toRender =
            <Tabs style={{activeTabContentStyle: {backgroundColor: '#006CEF', borderColor: '#006CEF', color: '#006CEF'}}}>
              <TabList>
                <Tab selectedClassName="tabselected">User Feedback</Tab>
                <Tab selectedClassName="tabselected">Company Feedback</Tab>
              </TabList>

              <TabPanel>
                <CompanyViewAccordion/>
              </TabPanel>
              <TabPanel>
                <CompanyFeedbackListViewAccordion/>
              </TabPanel>

            </Tabs>;
            //<CompanyViewAccordion/>
      }
    }
    else {
      toRender = <Tabs style={{activeTabContentStyle: {backgroundColor: '#006CEF', borderColor: '#006CEF', color: '#006CEF'}}}>
        <TabList>
          <Tab selectedClassName="tabselected">My Feedback</Tab>
          <Tab selectedClassName="tabselected">Feedback Forum</Tab>
        </TabList>

        <TabPanel>
          <MyFeedbacksTabAccordion/>
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
