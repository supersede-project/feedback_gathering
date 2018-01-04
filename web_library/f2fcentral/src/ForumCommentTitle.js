import React, {Component} from 'react';

import FaBug from 'react-icons/lib/fa/bug';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import FaHandPeaceO from 'react-icons/lib/fa/hand-peace-o';

class ForumCommentTitle extends Component {

  getIconForFeedbackType(type) {
      if (type === "Bug") {
          return <FaBug Icon size={35} padding={75}/>;
      }

      if (type === "Feature") {
          return <FaLightbulbO Icon size={35} padding={75}/>;
      }

      return <FaHandPeaceO Icon size={35} padding={75}/>;
  }


  render() {
    return (<div style={{display: "flex", justifyContent: "flex-start", padding: "20px", background: "#FFF"}}>
        <h5 align="left" style={{flexGrow: 2, fontSize: 12, fontStyle: 'italic'}}>{this.getIconForFeedbackType(this.props.type)}&nbsp; {this.props.title}</h5>
    </div>);
  }
}

export default ForumCommentTitle;
