import React, {Component} from 'react';

import FaBug from 'react-icons/lib/fa/bug';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import FaHandPeaceO from 'react-icons/lib/fa/hand-peace-o';
import FaThumbsOUp from 'react-icons/lib/fa/thumbs-o-up';
import FaThumbsODown from 'react-icons/lib/fa/thumbs-o-down';
import {toggleWidget} from 'react-chat-widget';
import FaCogs from 'react-icons/lib/fa/cogs';
import MdVisibilityOff from 'react-icons/lib/md/visibility-off';
import GoCircleSlash from 'react-icons/lib/go/circle-slash';
import MdCheckBoxOutlineBlank from 'react-icons/lib/md/check-box-outline-blank';
import MdVisibility from 'react-icons/lib/md/visibility';
import MdEmail from 'react-icons/lib/md/email';
import MdPublish from 'react-icons/lib/md/publish';

import style from './App.css';
import FeedbackSettings from "./FeedbackSettings";

 class CompanyViewFeedbackTitle extends Component {

  constructor(props) {
      super(props);
      this.state = {
          expanded: false,
          showSettings: false,
          visibleColor: 'black',
          showChat: false,
          lastPulled: null,
          visiblePublishedIcon: 'hidden'
      },
          this.toggleExpanded = this.toggleExpanded.bind(this);
      this.openSettings = this.openSettings.bind(this);
      this.closeThread = this.closeThread.bind(this);
      this.handleVisibility = this.handleVisibility.bind(this);
  }

  toggleExpanded()
  {
    this.setState({expanded: !this.state.expanded});
  }

  openSettings(){
     this.setState({showSettings: true})
      if(this.state.showSettings){
          return <div>
              <input type="checkbox"/>
              <label>No notification</label>
          </div>
      }
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

  closeThread(e){
  }


  handleMailIcon(){
      if(this.props.visibility === false){
         return <MdEmail size={35} color='black'/>;

      }
      if(this.props.visibility === true){
          return <MdEmail size={35} color='green'/>;
          }
  }

  handleVisibility(){
         if(this.props.visibility === false){
             if(this.props.published === false) {
                 this.setState({visiblePublishedIcon: 'hidden'});
                 return <MdVisibilityOff size={35}/>;
             }
         }

         if(this.props.visibility === true){
             if(this.props.published === false) {
                 this.setState({visiblePublishedIcon: 'visible'});
                 return <MdVisibilityOff size={35}/>;
             }
         }
         if(this.props.visibility === true) {
             if(this.props.published === true) {
                 this.setState({visiblePublishedIcon: 'hidden'});
                 return <MdVisibility size={35}/>
             }
         }
     }

     publishFeedback(){
         var that = this;
         fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/published/' + that.props.feedbackId, {
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
          <FaWechat align="left" size={35} color={'#63C050'} style={{flexGrow: "1"}} />
          {this.handleMailIcon()}
          <FaCogs size={35} onClick={this.openSettings.bind(this)}/>
      </div></div>);
  }
}

export default CompanyViewFeedbackTitle;
