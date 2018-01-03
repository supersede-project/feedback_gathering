import React, {Component} from 'react';

import FaBug from 'react-icons/lib/fa/bug';
import FaThumbsOUp from 'react-icons/lib/fa/thumbs-o-up';
import FaThumbsODown from 'react-icons/lib/fa/thumbs-o-down';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import FaHandPeaceO from 'react-icons/lib/fa/hand-peace-o';

import './App.css';

class ForumTitle extends Component {

    constructor(props) {
        super(props);
        this.handleShowCommentChange = this.handleShowCommentChange.bind(this);
        this.state = {
            thumbsUp: parseInt(this.props.thumbsUp),
            thumbsDown: parseInt(this.props.thumbsDown)
        }
    }

    handleShowCommentChange(e) {
        this.props.onShowCommentChange({showComment: true, index: this.props.index});
    }

    getIconForFeedbackType(type) {
        if (type === "Bug") {
            return <FaBug size={35} padding={75}/>;
        }

        if (type === "Feature") {
            return <FaLightbulbO size={35} padding={75}/>;
        }

        return <FaHandPeaceO size={35} padding={75}/>;
    }

    addLike(e) {
        this.setState({thumbsUp: this.state.thumbsUp + 1});
    }

    addDislike(e) {
        this.setState({thumbsDown: this.state.thumbsDown + 1});
    }

    render() {
        return (<div style={{display: "flex", justifyContent: "flex-start"}}>
            <h5 align="left" style={{
                flexGrow: 5,
                fontSize: 12,
                fontStyle: 'italic'
            }}>{this.getIconForFeedbackType(this.props.type)}&nbsp; {this.props.title}</h5>
            <div className="iconContainer" style={{flexGrow: 1, marginTop: '20px'}}>
                <FaWechat align="left" size={35} color={'#63C050'} onClick={this.handleShowCommentChange}/>
                <div className="thumbsIconContainer">
                    <FaThumbsOUp size={20} onClick={this.addLike}/>
                    <span className="thumbsCount">{this.state.thumbsUp}</span>
                </div>
                <div className="thumbsIconContainer">
                    <FaThumbsODown size={20} color={'black'} padding={10} onClick={this.addDislike}/>
                    <span className="thumbsCount">{this.state.thumbsDown}</span>
                </div>
            </div>
        </div>);
    }
}

export default ForumTitle;
