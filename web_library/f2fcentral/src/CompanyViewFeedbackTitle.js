import React, {Component} from 'react';

import TiInfoOutline from 'react-icons/lib/ti/info-outline';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import TiInfoLargeOutline from 'react-icons/lib/ti/info-large-outline';
import FaThumbsOUp from 'react-icons/lib/fa/thumbs-o-up';
import FaThumbsODown from 'react-icons/lib/fa/thumbs-o-down';
import TiGroupOutline from 'react-icons/lib/ti/group-outline';
import GoCircleSlash from 'react-icons/lib/go/circle-slash';
import TiTag from 'react-icons/lib/ti/tag';
import TiGroup from 'react-icons/lib/ti/group';
import MdEmail from 'react-icons/lib/md/email';
import MdPublish from 'react-icons/lib/md/publish';

import style from './App.css';

class CompanyViewFeedbackTitle extends Component {

  constructor(props) {
    super(props);
    this.state = {
      expanded: false,
      showSettings : false,
      showChat: false,
      lastPulled: null,
      visiblePublishedIcon: 'hidden',
      feedbackSetting: null
    }

    this.toggleExpanded = this.toggleExpanded.bind(this);
    this.closeThread = this.closeThread.bind(this);
    this.handleNewUserMessage = this.handleNewUserMessage.bind(this);
    this.handleShowChat = this.handleShowChat.bind(this);
    this.handleVisibility = this.handleVisibility.bind(this);
    this.fetchFeedbackSettings = this.fetchFeedbackSettings.bind(this);
    this.handleMailIcon = this.handleMailIcon.bind(this);
  }

    componentDidMount(){
      this.fetchFeedbackSettings();
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

  closeThread(e){
  }

  handleMailIcon(){
        if(this.state.feedbackSetting ===  null || this.state.feedbackSetting.feedbackQuery === false) {
              return <MdEmail size={35} color='black'/>;
        }
          if(this.state.feedbackSetting.feedbackQueryChannel === "Email"){
              return <MdEmail size={35} color='green'/>;
          }
          else {
              return <MdEmail size={35} color='black'/>;
          }
      }
      /*if(this.state.feedbackQuery === 'Feedback-To-Feedback Central'){
          return <MdEmail size={35} color='black'/>;
      }

    if(this.state.feedbackQuery === 'Email'){
      return <MdEmail size={35} color='green'/>;
    }*/



  handleVisibility(){
    if(this.props.visibility === false){
      if(this.props.published === false) {
        if(this.state.visiblePublishedIcon !== 'hidden') {
            this.setState({visiblePublishedIcon: 'hidden'});
        }
        return <TiGroupOutline size={35}/>;
      }
    }

    if(this.props.visibility === true){
      if(this.props.published === false) {
        if(this.state.visiblePublishedIcon !== 'visible') {
            this.setState({visiblePublishedIcon: 'visible'});
        }
        return <TiGroupOutline size={35}/>;
      }
    }
    if(this.props.visibility === true) {
      if(this.props.published === true) {
        if(this.state.visiblePublishedIcon !== 'hidden') {
            this.setState({visiblePublishedIcon: 'hidden'});
        }
        return <TiGroup size={35}/>
      }
    }
  }

  fetchFeedbackSettings(){
      //feedbacksettings/feedback/
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

  publishFeedback(){
    var that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/published/' + this.props.feedbackId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      },
      body: JSON.stringify({
        published: true
      })
    }).then(result=> that.props.update());
    e.stopPropagation();
  }

  render()
  {
    var showChat = null;
    return (<div style={{display: "flex", justifyContent: "flex-start"}}><h5 align="left" style={{
      flexGrow: 2,
      fontSize: 12,
      fontStyle: 'italic'
    }} onClick={this.toggleExpanded}><GoCircleSlash size={30} color="red" onClick={this.closeThread}/>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 20)? this.props.title.substring(0, 20) + "...": this.props.title}
    <div><div align="left" style={{fontSize: 10}}>sent on {this.props.date}</div>
    <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Status: {this.props.status}</div>
    <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Forum activity:
      <FaThumbsOUp size={20} color={'black'} padding={10}/>
      <span className={style.counts}>{this.props.likes}</span>
      <FaThumbsODown size={20} color={'black'} padding={10}/>
      <span className={style.counts}>{this.props.dislikes}</span>
      <FaWechat size={20} color={'#63C050'} padding={10}/>
      <span className={style.counts}>{this.props.commentnumber}</span>
    </div></div></h5>

    <div className="companyIconContainer">
      {this.handleVisibility()}
      <MdPublish className={style.counts} size={20} padding={10} visibility={this.state.visiblePublishedIcon} onClick={this.publishFeedback}/>
      <FaWechat align="left" size={35} color={'#63C050'} style={{flexGrow: "1"}} onClick={this.handleShowChat}/>
      {this.handleMailIcon()}
    </div></div>);
  }
}

export default CompanyViewFeedbackTitle;
