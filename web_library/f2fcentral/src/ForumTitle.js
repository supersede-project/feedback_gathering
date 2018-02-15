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
            expanded: false,
            thumbsUp: parseInt(this.props.thumbsUp),
            thumbsDown: parseInt(this.props.thumbsDown),
            comment: parseInt(this.props.comment),
            unread: false,
            unreadFeedback: null
        }
        this.toggleExpanded = this.toggleExpanded.bind(this);
        this.fetchUnreadFeedback = this.fetchUnreadFeedback.bind(this);
    }

    componentDidMount(){
        this.fetchUnreadFeedback();
    }

    componentWillReceiveProps(nextProps) {
      this.setState({ thumbsUp: parseInt(nextProps.thumbsUp),
      thumbsDown: parseInt(nextProps.thumbsDown), comment: parseInt(nextProps.comment) });
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

    fetchUnreadFeedback(){
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/get_published/unread/user/' + sessionStorage.getItem('userId'), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result=>result.json())
            .then(result=> {
                var unread = false;
                if(result.id === that.props.feedbackId){
                    unread = true;
                }
                that.setState({unreadFeedback: result, unread: unread})
            });
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

    toggleExpanded()
    {
        this.setState({expanded: !this.state.expanded});
    }



    render() {
        let toRender = null;

        if(!this.state.unread) {
            return (<div style={{display: "flex", justifyContent: "flex-start"}}>
                <h5 align="left" style={{
                    flexGrow: 2,
                    fontSize: 12,
                    fontStyle: 'italic'
                }}
                    onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 20) ? this.props.title.substring(0, 20) + "..." : this.props.title}
                </h5>
                <div className="forumIconContainer">
                    <FaWechat align="left" size={35} style={{flexGrow: "1"}} color={'#63C050'}
                              onClick={this.handleShowCommentChange}/>
                    <span className={style.thumbsCount}>{this.state.comment}</span>
                    <div className={style.forumIconContainer.thumbsIconContainer}>
                        <FaThumbsOUp size={20} onClick={this.addLike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsUp}</span>
                    </div>
                    <div className={style.forumIconContainer.thumbsIconContainer}>
                        <FaThumbsODown size={20} onClick={this.addDislike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsDown}</span>
                    </div>
                </div>
            </div>);
        }
        else if(this.state.unread){

            return (<div style={{display: "flex", justifyContent: "flex-start", background: '#1a8cff'}}>
                <h5 align="left" style={{
                    flexGrow: 2,
                    fontSize: 12,
                    fontStyle: 'italic'
                }}
                    onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 20) ? this.props.title.substring(0, 20) + "..." : this.props.title}
                </h5>
                <div className="forumIconContainer">
                    <FaWechat align="left" size={35} style={{flexGrow: "1"}} color={'#63C050'}
                              onClick={this.handleShowCommentChange}/>
                    <span className={style.thumbsCount}>{this.state.comment}</span>
                    <div className={style.forumIconContainer.thumbsIconContainer}>
                        <FaThumbsOUp size={20} onClick={this.addLike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsUp}</span>
                    </div>
                    <div className={style.forumIconContainer.thumbsIconContainer}>
                        <FaThumbsODown size={20} onClick={this.addDislike}/>
                        <span className={style.thumbsCount}>{this.state.thumbsDown}</span>
                    </div>
                </div>
            </div>);
        }
    }
}

export default ForumTitle;
