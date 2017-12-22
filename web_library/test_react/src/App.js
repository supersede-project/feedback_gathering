import React, { Component } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';

import '../../node_modules/react-accessible-accordion/dist/react-accessible-accordion.css';
import { Widget, addResponseMessage } from 'react-chat-widget';

class App extends Component {



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

                    </TabPanel>
                    <TabPanel>
                    </TabPanel>

                </Tabs>
            </div>
        );
    }
}

export default App;
