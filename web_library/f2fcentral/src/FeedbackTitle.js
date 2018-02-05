import React, {Component} from 'react';

import FaBug from 'react-icons/lib/fa/bug';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import FaHandPeaceO from 'react-icons/lib/fa/hand-peace-o';
import { Widget, addResponseMessage, addUserMessage, toggleWidget } from 'react-chat-widget';
import FaCogs from 'react-icons/lib/fa/cogs';
import MdVisibilityOff from 'react-icons/lib/md/visibility-off';
import MdCheckBoxOutlineBlank from 'react-icons/lib/md/check-box-outline-blank';
import MdEmail from 'react-icons/lib/md/email';
import MdVisibility from 'react-icons/lib/md/visibility';

import './App.css';

class FeedbackTitle extends Component {

  constructor(props) {
    super(props);
    this.state = {
      expanded: false,
      iconColor: 'black',
      showChat: false,
      lastPulled: null
    }
    this.toggleExpanded = this.toggleExpanded.bind(this);
    this.changeMailSetting = this.changeMailSetting.bind(this);
    this.setVisibility = this.setVisibility.bind(this);
    this.showChatWindow = this.showChatWindow.bind(this);
    this.fetchResponses = this.fetchResponses.bind(this);
    this.handleNewUserMessage = this.handleNewUserMessage.bind(this);
  }

  componentDidMount() {

  }

  handleNewUserMessage(newMessage) {
    var that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/feedback_chat', {
      header: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      },
      method: 'POST',
      body: JSON.stringify({
        feedback_id: that.props.feedbackId,
        user_id: sessionStorage.getItem('userId'),
        chat_text: newMessage,
        initiated_by_user: false
      })
    })
  }


  toggleExpanded()
  {
    this.setState({expanded: !this.state.expanded});
  }

  //bug=661, function=662, generalfeedback=663
  getIconForFeedbackType() {
    if (this.props.type === 661) {
      return <FaBug size={35} padding={75}/>;
    }

    if (this.props.type === 662) {
      return <FaLightbulbO size={35} padding={75}/>;
    }
    if(this.props.type === 663) {
      return <FaHandPeaceO size={35} padding={75}/>;
    }
    return <MdCheckBoxOutlineBlank size={35} padding={75} visibility="hidden"/>;
  }

  showChatWindow(e) {
    if(!this.state.showChat) {
      fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedback_chat/feedback/' + this.props.feedbackId, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': sessionStorage.getItem('token')
        }
      }).then(result=>result.json())
      .then(result=> {
        result.sort((a, b) => {
          if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") < new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return -1;
          if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") > new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return 1;
          return 0;
        })
        result.map((item, index) => {
          if(item.user.id === parseInt(sessionStorage.getItem('userId'))) {
            addUserMessage(item.chatText);
          }
          else {
            addResponseMessage(item.chatText);
          }
        })
      });
    }
    this.setState({showChat: true, lastPulled: new Date()});
    toggleWidget();
    setInterval(this.fetchResponses, 3000);
  }

  fetchResponses() {
    var that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedback_chat/feedback/' + this.props.feedbackId, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      }
    }).then(result=>result.json())
    .then(result=> {
      result.sort((a, b) => {
        if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") < new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return -1;
        if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") > new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return 1;
        return 0;
      })
      result.map((item, index) => {
        if(that.state.lastPulled < new Date(item.chatDate.substring(0, item.chatDate.indexOf('.')) + "Z") && item.user.id !== sessionStorage.getItem('userId')) {
          addResponseMessage(item.chatText);
        }
      })
    })
    that.setState({lastPulled: new Date()});
  }

  changeMailSetting(){
    if(this.state.iconColor==='black') {
      this.setState({
        iconColor: 'green',


      });
    }
    if(this.state.iconColor==='green'){
      this.setState({
        iconColor: 'black'
      });
    }
  }

  handleVisibility(){
    if(this.props.visibility === false){
      return <MdVisibilityOff size={35} onClick={this.setVisibility}/>;
    }
    if(this.props.visibility === true){
      return <MdVisibility size={35}/>
    }
  }

  setVisibility(e){
    var that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/visibility/' + that.props.feedbackId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      },
      body: JSON.stringify({
        visible: true
      })
    }).then(result=> that.props.update());
    e.stopPropagation();
  }

  render()
  {
    var showChat = null;
    if(this.state.showChat) {
      showChat = <Widget title={this.props.title} subtitle="" handleNewUserMessage={this.handleNewUserMessage}/>
    }
    return (<div style={{display: "flex", justifyContent: "flex-start"}}><h5 align="left" style={{
      flexGrow: 2,
      fontSize: 12,
      fontStyle: 'italic'
    }} onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 20)? this.props.title.substring(0, 20) + "...": this.props.title}
    <div><div align="left" style={{fontSize: 10}}>sent on {this.props.date}</div>
    <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Status: {this.props.status}</div>
  </div></h5>
  {showChat}
  <div className="iconContainer">
    {this.handleVisibility()}
    <MdEmail size={35} onClick={this.changeMailSetting} color={this.state.iconColor}/>
    <FaWechat align="left" color={'#63C050'} style={{flexGrow: "1"}} onClick={this.showChatWindow} size={35}/>
    <FaCogs size={35}/></div></div>);
    }
  }

  export default FeedbackTitle;
