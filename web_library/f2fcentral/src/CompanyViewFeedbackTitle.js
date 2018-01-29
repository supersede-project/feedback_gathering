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
import FaClose from 'react-icons/lib/fa/close';
import FaFileImageO from 'react-icons/lib/fa/file-image-o';
import GoCircleSlash from 'react-icons/lib/go/circle-slash';
//import FileInput from 'react-file-input';

import style from './App.css';
import FeedbackSettings from "./FeedbackSettings";

 class CompanyViewFeedbackTitle extends Component {

  constructor(props) {
    super(props);
    this.state = {
      expanded: false,
        showSettings : false
    }

    this.toggleExpanded = this.toggleExpanded.bind(this);
    this.openSettings = this.openSettings.bind(this);
    this.openFileDialog = this.openFileDialog.bind(this);
    this.closeThread = this.closeThread.bind(this);
  }

  handleChange() {

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
  }

  closeThread(e){
  }

  openFileDialog(e){


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
      <MdVisibilityOff size={35}/><FaWechat align="left" size={35} color={'#63C050'} style={{flexGrow: "1"}} onClick={toggleWidget}/><FaCogs size={35} onClick={this.openSettings.bind(this)}/>
      <FaFileImageO size={35} align="left" onClick={this.openFileDialog}/>
      </div></div>);
  }
}

export default CompanyViewFeedbackTitle;
