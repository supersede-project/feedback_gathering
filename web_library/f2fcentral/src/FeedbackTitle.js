import React, {Component} from 'react';

import FaBug from 'react-icons/lib/fa/bug';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import FaHandPeaceO from 'react-icons/lib/fa/hand-peace-o';
import {toggleWidget} from 'react-chat-widget';
import FaCogs from 'react-icons/lib/fa/cogs';
import MdVisibilityOff from 'react-icons/lib/md/visibility-off';
import MdCheckBoxOutlineBlank from 'react-icons/lib/md/check-box-outline-blank';
import MdEmail from 'react-icons/lib/md/email';
import MdNotificationsActive from 'react-icons/lib/md/notifications-active';
import MdVisibility from 'react-icons/lib/md/visibility';

import './App.css';

 class FeedbackTitle extends Component {

  constructor(props) {
    super(props);
    this.state = {
        expanded: false,
        iconColor: 'black',

    }
    this.toggleExpanded = this.toggleExpanded.bind(this);
    this.changeMailSetting = this.changeMailSetting.bind(this);
    this.setVisibility = this.setVisibility.bind(this);
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

      return (<div style={{display: "flex", justifyContent: "flex-start"}}><h5 align="left" style={{
          flexGrow: 2,
          fontSize: 12,
          fontStyle: 'italic'
      }} onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 20)? this.props.title.substring(0, 20) + "...": this.props.title}
          <div><div align="left" style={{fontSize: 10}}>sent on {this.props.date}</div>
              <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Status: {this.props.status}</div>
          </div></h5>
          <div className="iconContainer">
          {this.handleVisibility()}
          <MdEmail size={35} onClick={this.changeMailSetting} color={this.state.iconColor}/>
          <MdNotificationsActive size={35}/><FaWechat align="left" color={'#63C050'} style={{flexGrow: "1"}} onClick={toggleWidget} size={35}/>
          <FaCogs size={35}/></div></div>);
  }
}

export default FeedbackTitle;
