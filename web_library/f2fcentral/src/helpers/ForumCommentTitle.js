import React, {Component} from 'react';

import TiInfoOutline from 'react-icons/lib/ti/info-outline';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import TiInfoLargeOutline from 'react-icons/lib/ti/info-large-outline';
import TiTag from 'react-icons/lib/ti/tag';


class ForumCommentTitle extends Component {

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

  render() {
    return (
        <div style={{display: "flex", justifyContent: "flex-start", padding: "20px", background: "#FFF"}}>
        <h4 align="left" style={{flexGrow: 2, fontSize: 12, fontStyle: 'italic'}}>{this.getIconForFeedbackType(this.props.type)}&nbsp; {this.props.title}</h4>
    </div>);
  }
}

export default ForumCommentTitle;
