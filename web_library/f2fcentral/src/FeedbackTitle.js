import React, {Component} from 'react';

import TiInfoOutline from 'react-icons/lib/ti/info-outline';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import TiInfoLargeOutline from 'react-icons/lib/ti/info-large-outline';
import { Widget, addResponseMessage, addUserMessage, toggleWidget } from 'react-chat-widget';
import TiGroupOutline from 'react-icons/lib/ti/group-outline';
import TiTag from 'react-icons/lib/ti/tag';
import MdEmail from 'react-icons/lib/md/email';
import TiGroup from 'react-icons/lib/ti/group';
import FaThumbsOUp from 'react-icons/lib/fa/thumbs-o-up';
import FaThumbsODown from 'react-icons/lib/fa/thumbs-o-down';

import style from './App.css';

class FeedbackTitle extends Component {

  constructor(props) {
    super(props);
    this.state = {
      expanded: false,
      iconColor: 'black',
      showChat: false,
      feedbackSetting : null,
      feedbackStatus : null,
      lastPulled: null
    }
    this.toggleExpanded = this.toggleExpanded.bind(this);
    this.setVisibility = this.setVisibility.bind(this);
    this.handleNewUserMessage = this.handleNewUserMessage.bind(this);
      this.handleShowChat = this.handleShowChat.bind(this);
      this.handleMailIcon = this.handleMailIcon.bind(this);
      this.fetchFeedbackSettings = this.fetchFeedbackSettings.bind(this);
      this.handleFeedbackStatus = this.handleFeedbackStatus.bind(this);

  }

  //Ensure request is sent upon loading of component
    componentDidMount(){
     this.fetchFeedbackSettings();
     this.fetchFeedbackStatus();
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

    handleShowChat(e) {
        this.props.onShowChat({showChat: true, index: this.props.feedbackId, title: this.props.title});
    }

  toggleExpanded()
  {
    this.setState({expanded: !this.state.expanded});
  }

  //bug=661, function=662, generalfeedback=663
  getIconForFeedbackType() {
    if (this.props.type === 661) {
      return <TiInfoOutline size={35} padding={75}/>;
    }

    if (this.props.type === 662) {
      return <FaLightbulbO size={35} padding={75}/>;
    }
    if(this.props.type === 663) {
      return <TiInfoLargeOutline size={35} padding={75}/>;
    }
    return <TiTag size={35} padding={75}/>;
  }

  handleVisibility(){
    if(this.props.visibility === false){
      return <TiGroupOutline size={35} onClick={this.setVisibility}/>;
    }
    if(this.props.visibility === true){
      return <TiGroup size={35}/>
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

    handleMailIcon(){
        if(this.state.feedbackSetting ===  null || this.state.feedbackSetting.feedbackQuery === false) {
            return <MdEmail size={35} color='black' onClick={this.changeMailSetting}/>;
        }
        if(this.state.feedbackSetting.feedbackQueryChannel === "Email"){
            return <MdEmail size={35} color='green' onClick={this.changeMailSetting}/>;
        }
        else {
            return <MdEmail size={35} color='black' onClick={this.changeMailSetting}/>;
        }
    }

    fetchFeedbackSettings(){
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedbacksettings/feedback/' + this.props.feedbackId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result=>result.json())
            .then(result=> {
                that.setState({feedbackSetting: result})
            });
    }

    changeMailSetting(e){
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedbacksettings/',  {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            },
            body: JSON.stringify({
                statusUpdates: true,
                statusUpdatesContactChannel: 'Email',
                feedbackQuery: true,
                feedbackQueryChannel: 'Email',
                feedback_id: that.props.feedbackId
            })
        }).then(result=> that.props.updateSetting());
        e.stopPropagation();
    }

    fetchFeedbackStatus(){
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/status/feedback/' + this.props.feedbackId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result=>result.json())
            .then(result=> {
                that.setState({feedbackStatus: result})
            });
    }

    handleFeedbackStatus(){
        //var that = this;
        if(this.state.feedbackStatus === null || this.state.feedbackStatus.status === null){
            return <label className={style.statusunknown}>Status loading</label>
        }
        if(this.state.feedbackStatus.status === 'completed'){
            return <label className={style.statuscomplete}>Completed</label>
        }
        if(this.state.feedbackStatus.status === 'in_progress'){
            return <label className={style.statusprogress}>In Progress</label>
        }
        if(this.state.feedbackStatus.status === 'declined'){
            return <label className={style.statusdeclined}>Declined</label>
        }
        if(this.state.feedbackStatus.status === 'received'){
            return <label className={style.statusreceived}>Received</label>
        }
        else {
            return <label className={style.statusunknown}>No Status available</label>
        }
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
    <div className={style.spacingstyle}><div align="left" style={{fontSize: 10}}>sent on {this.props.date}</div>
    <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Status: {this.handleFeedbackStatus()}</div>
      <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Forum activity:
        <FaThumbsOUp size={20} color={'black'} padding={10}/>
        <span className={style.counts}>{this.props.likes}</span>
        <FaThumbsODown size={20} color={'black'} padding={10}/>
        <span className={style.counts}>{this.props.dislikes}</span>
        <FaWechat size={20} color={'#63C050'} padding={10}/>
        <span className={style.counts}>{this.props.commentnumber}</span>
      </div>
  </div></h5>
  {showChat}
  <div className="iconContainer">
    {this.handleVisibility()}
    {/*
    <MdEmail size={35} onClick={this.changeMailSetting} color={this.state.iconColor}/>
    */}
      {this.handleMailIcon()}
    <FaWechat align="left" color={'#63C050'} style={{flexGrow: "1"}} onClick={this.handleShowChat} size={35}/>
    </div></div>);
    }
  }

  export default FeedbackTitle;
