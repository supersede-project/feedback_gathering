import React, {Component} from 'react';

import FaBug from 'react-icons/lib/fa/bug';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import FaHandPeaceO from 'react-icons/lib/fa/hand-peace-o';

class ForumCommentTitle extends Component {

  getIconForFeedbackType() {
      if (this.props.type === 1) {
          return <FaBug size={35} padding={75}/>;
      }

      if (this.props.type === 2) {
          return <FaLightbulbO size={35} padding={75}/>;
      }

      return <FaHandPeaceO size={35} padding={75}/>;
  }


  render() {
    return (<div style={{display: "flex", justifyContent: "flex-start", padding: "20px", background: "#FFF"}}>
        <h5 align="left" style={{flexGrow: 2, fontSize: 12, fontStyle: 'italic'}}>{this.getIconForFeedbackType(this.props.type)}&nbsp; {this.props.title}</h5>
    </div>);
  }
}

export default ForumCommentTitle;
