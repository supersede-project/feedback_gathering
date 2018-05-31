import React, {Component} from 'react';

import TiInfoOutline from 'react-icons/lib/ti/info-outline';
import FaThumbsOUp from 'react-icons/lib/fa/thumbs-o-up';
import FaThumbsODown from 'react-icons/lib/fa/thumbs-o-down';
import TiMessages from 'react-icons/lib/ti/messages';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import TiInfoLargeOutline from 'react-icons/lib/ti/info-large-outline';
import TiTag from 'react-icons/lib/ti/tag';
import MdVisibility from 'react-icons/lib/md/visibility';


import style from '../css/App.css';

class ForumTitle extends Component {

    constructor(props) {
        super(props);
        this.handleShowCommentChange = this.handleShowCommentChange.bind(this);
        this.addLike = this.addLike.bind(this);
        this.addDislike = this.addDislike.bind(this);
        this.state = {
            expanded: false,
            thumbsUp: parseInt(this.props.thumbsUp),
            thumbsDown: parseInt(this.props.thumbsDown),
            comment: parseInt(this.props.comment)
        }
        this.toggleExpanded = this.toggleExpanded.bind(this);
        this.checkStatus = this.checkStatus.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            thumbsUp: parseInt(nextProps.thumbsUp),
            thumbsDown: parseInt(nextProps.thumbsDown), comment: parseInt(nextProps.comment)
        });
    }

    handleShowCommentChange(e) {
        if (!this.props.visibility) {
            this.props.onShowCommentChange({showComment: true, index: this.props.feedbackId});
        }
    }

    getIconForFeedbackType() {
        if (this.props.type === 661) {
            return <TiInfoOutline size={35} padding={75}/>;
        }

        if (this.props.type === 662) {
            return <FaLightbulbO size={35} padding={75}/>;
        }
        if (this.props.type === 663) {
            return <TiInfoLargeOutline size={35} padding={75}/>;
        }
        return <TiTag size={35} padding={75}/>;
    }

    checkStatus(response){
        if (response.status >= 200 && response.status < 300) {
            return response
        } else {
            var error = new Error(response.statusText)
            error.response = response
            throw error
        }
    }

    addLike(e) {
        if (!this.props.visibility) {
            fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/likes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem('token')
                },
                body: JSON.stringify({
                    user_id: sessionStorage.getItem('userId'),
                    feedback_id: this.props.feedbackId
                })
            }).then(this.checkStatus)
                .then(result => result.json())
                .then(result => {
                    this.props.update();
                })
                .catch(error => {
                    console.log('request failed')
                    this.props.update();
                });
        }
        e.stopPropagation();
    }

    addDislike(e) {
        if (!this.props.visibility) {
            fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/dislikes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem('token')
                },
                body: JSON.stringify({
                    user_id: sessionStorage.getItem('userId'),
                    feedback_id: this.props.feedbackId
                })
            }).then(this.checkStatus)
                .then(result => result.json())
                .then(result => {
                    this.props.update();
                })
                .catch(error => {
                    console.log('request failed')
                    this.props.update();
                });
        }
        e.stopPropagation();
    }

    toggleExpanded(e) {
        if (!this.props.visibility) {
            this.setState({expanded: !this.state.expanded});
            fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/feedback_views', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem('token')
                },
                body: JSON.stringify({
                    user_id: sessionStorage.getItem('userId'),
                    feedback_id: this.props.feedbackId
                })
            }).then(result => result.json())
                .then(result => {
                    this.props.update();
                });
        }
        e.stopPropagation();
    }


    render() {
        let toRender = null;

         if (!this.props.unread) {
            return (<div style={{display: "flex", justifyContent: "space-around", background: 'FFFFF'}}>
                <h5 align="left" style={{
                    flexGrow: 2,
                    fontSize: 12,
                    fontStyle: 'italic'
                }}
                    onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 30) ? this.props.title.substring(0, 30) + "..." : this.props.title}
                </h5>
                <div className={style.forumIconContainer}>
                    <div className={style.bundledIcon}>
                        <TiMessages align="left" size={35} style={{flexGrow: "1"}} color={'#006CEF'}
                                  onClick={this.handleShowCommentChange}/>
                        <span className={style.thumbsCount}>{this.state.comment}</span>
                    </div>

                    <div className={style.bundledIcon}>
                        <FaThumbsOUp size={35} onClick={this.addLike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsUp}</span>
                    </div>

                    <div className={style.bundledIcon}>
                        <FaThumbsODown size={35} onClick={this.addDislike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsDown}</span>
                    </div>
                </div>
            </div>);
        }
        else if(this.props.blocked === true){
            return (<div style={{display: "flex", justifyContent: "space-around", background: '#b300b3'}}>
                <h5 align="left" style={{
                    flexGrow: 2,
                    fontSize: 12,
                    fontStyle: 'italic'
                }}
                    onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 30) ? this.props.title.substring(0, 30) + "..." : this.props.title}

                    <div className={style.spacingstyle}>
                        <div align="left" style={{fontSize: 10, color: 'black'}}>
                            <MdVisibility size={17} color={"black"}/>Status: Feedback blocked</div>
                    </div></h5>
                <div className={style.forumIconContainer}>
                    <div className={style.bundledIcon}>
                        <TiMessages align="left" size={35} style={{flexGrow: "1"}} color={'#006CEF'}
                                  onClick={this.handleShowCommentChange}/>
                        <span className={style.thumbsCount}>{this.state.comment}</span>
                    </div>

                    <div className={style.bundledIcon}>
                        <FaThumbsOUp size={35} onClick={this.addLike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsUp}</span>
                    </div>

                    <div className={style.bundledIcon}>
                        <FaThumbsODown size={35} onClick={this.addDislike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsDown}</span>
                    </div>
                </div>
            </div>);
        }
        else {

            return (<div style={{display: "flex", justifyContent: "space-around", background: '#EFF6FC'}}>
                <h5 align="left" style={{
                    flexGrow: 2,
                    fontSize: 12,
                    fontStyle: 'italic'
                }}
                    onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 30) ? this.props.title.substring(0, 30) + "..." : this.props.title}
                </h5>
                <div className={style.forumIconContainer}>
                    <div className={style.bundledIcon}>
                        <TiMessages align="left" size={35} style={{flexGrow: "1"}} color={'#006CEF'}
                                  onClick={this.handleShowCommentChange}/>
                        <span className={style.thumbsCount}>{this.state.comment}</span>
                    </div>

                    <div className={style.bundledIcon}>
                        <FaThumbsOUp size={35} onClick={this.addLike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsUp}</span>
                    </div>

                    <div className={style.bundledIcon}>
                        <FaThumbsODown size={35} onClick={this.addDislike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsDown}</span>
                    </div>
                </div>
            </div>);
        }
    }
}

export default ForumTitle;
