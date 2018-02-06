import React, {Component} from 'react';
import { PulseLoader } from 'react-spinners';

import { Accordion, AccordionItem } from 'react-sanfona';

import './App.css';
import 'react-tabs/style/react-tabs.css';

import 'react-accessible-accordion/dist/react-accessible-accordion.css';
import FeedbackTitle from './FeedbackTitle';
import FeedbackBody from './FeedbackBody';
import FeedbackData from './FeedbackData';
import MdNotificationsActive from 'react-icons/lib/md/notifications-active';
import ChatView from './ChatView';


class MyFeedbacksTabAccordion extends Component {

    constructor(props) {
        super(props);
        this.state = {
          data : [],
          loading: true,
          showChat: false,
          chatIndex: null,
          chatTitle: ''
        }
        this.fetchData = this.fetchData.bind(this);
        this.handleShowChat = this.handleShowChat.bind(this);
        this.handleBackButtonPressed = this.handleBackButtonPressed.bind(this);
    }

    componentDidMount() {
      this.fetchData(null);
    }

    fetchData(e) {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/user_identification/' + sessionStorage.getItem('userId'), {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result=>result.json())
            .then(result=> {
                that.setState({data: result, loading: false})
            });
    }

    disableNotifications(){
        return <MdNotificationsActive size={35} align="right" color='black'/>;
    }

    handleShowChat(e) {
        this.setState({showChat: e.showChat, chatIndex: e.index, chatTitle: e.title});
    }

    handleBackButtonPressed() {
        this.setState({showChat: false, chatIndex: null, chatTitle: ''});
    }

    render() {
      let toRender = null;
      var that = this;
      if(!that.state.showChat && that.state.data.length > 0) {

        toRender = <Accordion>
        {that.state.data.map(function (item, index) {
            if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
            {
              return (
                  <AccordionItem titleTag="span" title={<FeedbackTitle feedbackId={item.id} update={that.fetchData} updateSetting={that.fetchData} onShowChat={that.handleShowChat} type={item.categoryFeedbacks[0].parameterId} title={item.textFeedbacks[0].text} visibility={item.visibility} date={item.createdAt} status="WIP" likes={item.likeCount} dislikes={item.dislikeCount} commentnumber={item.commentCount}/>}>
                  </AccordionItem>
              )
            }
            return false;
        })}
                  </Accordion>;
      }
      else if (this.state.loading) {
        var divStyle = {
          position: 'absolute',
          top: '50%',
          left: '50%',
          marginRight: '-50%',
          transform: 'translate(-50%, -50%)'
        }
        toRender = <div style={divStyle}><PulseLoader
          loading={this.state.loading}
        /></div>
      }
      else if(this.state.showChat) {
          toRender = <ChatView feedbackId={this.state.chatIndex} title={this.state.chatTitle} onBackButtonPressed={this.handleBackButtonPressed}/>
      }
      else {
        toRender = <p>No Elements to show</p>;
      }
        return (
            <div className="MyFeedbacksTabAccordion">
                <MdNotificationsActive size={35} align="right" color='green' onClick={this.disableNotifications}/>
                <div className="MyFeedback">
                <ul>
                    {toRender}
                    </ul>
                </div>
            </div>
        );
    }
}

export default MyFeedbacksTabAccordion;
