import React, {Component} from 'react';

import FaBug from 'react-icons/lib/fa/bug';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import FaHandPeaceO from 'react-icons/lib/fa/hand-peace-o';
import {toggleWidget} from 'react-chat-widget';
import FaCogs from 'react-icons/lib/fa/cogs';
import MdVisibilityOff from 'react-icons/lib/md/visibility-off';

class FeedbackTitle extends Component {

  constructor(props) {
    super(props);
    this.state = {
      expanded: false
    }
    this.toggleExpanded = this.toggleExpanded.bind(this);
  }

  toggleExpanded()
  {
    this.setState({expanded: !this.state.expanded});
  }

  getIconForFeedbackType() {
      if (this.props.type === 1) {
          return <FaBug size={35} padding={75}/>;
      }

      if (this.props.type === 2) {
          return <FaLightbulbO size={35} padding={75}/>;
      }

      return <FaHandPeaceO size={35} padding={75}/>;
  }

  render()
  {
      return (<div style={{display: "flex", justifyContent: "flex-start"}}><h5 align="left" style={{
          flexGrow: 2,
          fontSize: 12,
          fontStyle: 'italic'
      }} onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 20)? this.props.title.substring(0, 20) + "...": this.props.title}</h5>
      <MdVisibilityOff size={35}/><FaWechat align="left" size={35} color={'#63C050'} style={{flexGrow: "1"}} onClick={toggleWidget}/><FaCogs size={35}/></div>);
  }
}

export default FeedbackTitle;
