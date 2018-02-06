import React, {Component} from 'react';

import TiInfoOutline from 'react-icons/lib/ti/info-outline';
import FaThumbsOUp from 'react-icons/lib/fa/thumbs-o-up';
import FaThumbsODown from 'react-icons/lib/fa/thumbs-o-down';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import TiInfoLargeOutline from 'react-icons/lib/ti/info-large-outline';
import TiTag from 'react-icons/lib/ti/tag';


import style from './App.css';

class ForumTitle extends Component {

    constructor(props) {
        super(props);
        this.handleShowCommentChange = this.handleShowCommentChange.bind(this);
        this.addLike = this.addLike.bind(this);
        this.addDislike = this.addDislike.bind(this);
        this.state = {
            thumbsUp: parseInt(this.props.thumbsUp),
            thumbsDown: parseInt(this.props.thumbsDown)
        }
    }

    componentWillReceiveProps(nextProps) {
      this.setState({ thumbsUp: parseInt(nextProps.thumbsUp),
      thumbsDown: parseInt(nextProps.thumbsDown) });
    }

    handleShowCommentChange(e) {
        this.props.onShowCommentChange({showComment: true, index: this.props.feedbackId});
    }

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

    addLike(e) {
      fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/likes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': sessionStorage.getItem('token')
        },
        body: JSON.stringify({
          user_id: sessionStorage.getItem('userId'),
          feedback_id: this.props.feedbackId
        })
      }).then(result=>result.json())
      .then(result=> {
        this.props.update();
      });
        e.stopPropagation();
    }

    addDislike(e) {
      fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/dislikes', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': sessionStorage.getItem('token')
        },
        body: JSON.stringify({
          user_id: sessionStorage.getItem('userId'),
          feedback_id: this.props.feedbackId
        })
      }).then(result=>result.json())
      .then(result=> {
        this.props.update();
      });
        e.stopPropagation();
    }

    render() {
        return (<div style={{display: "flex", justifyContent: "flex-start"}}>
            <h5 align="left" style={{
                flexGrow: 5,
                fontSize: 12,
                fontStyle: 'italic'
            }}>{this.getIconForFeedbackType(this.props.type)}&nbsp; {this.props.title}</h5>
            <div className={style.iconContainer} style={{flexGrow: 1, marginTop: '20px'}}>
                <FaWechat align="left" size={35} color={'#63C050'} onClick={this.handleShowCommentChange}/>
                <div className={style.thumbsIconContainer}>
                    <FaThumbsOUp size={20} onClick={this.addLike}/>
                    <span className={style.thumbsCount}>{this.state.thumbsUp}</span>
                </div>
                <div className={style.thumbsIconContainer}>
                    <FaThumbsODown size={20} onClick={this.addDislike}/>
                    <span className={style.thumbsCount}>{this.state.thumbsDown}</span>
                </div>
            </div>
        </div>);
    }
}

export default ForumTitle;
