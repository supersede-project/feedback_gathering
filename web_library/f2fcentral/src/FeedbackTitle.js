import React, {Component} from 'react';

import TiInfoOutline from 'react-icons/lib/ti/info-outline';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import TiInfoLargeOutline from 'react-icons/lib/ti/info-large-outline';
import {Widget, addResponseMessage, addUserMessage, toggleWidget} from 'react-chat-widget';
import TiGroupOutline from 'react-icons/lib/ti/group-outline';
import TiTag from 'react-icons/lib/ti/tag';
import MdEmail from 'react-icons/lib/md/email';
import TiGroup from 'react-icons/lib/ti/group';
import FaThumbsOUp from 'react-icons/lib/fa/thumbs-o-up';
import FaThumbsODown from 'react-icons/lib/fa/thumbs-o-down';

import style from './App.css';

class FeedbackTitle extends Component {

    constructor(props) {
        super(props);
        this.state = {
            expanded: false,
            iconColor: 'black',
            showChat: false,
            feedbackSetting: null,
            feedbackStatus: null,
            unreadChat: [],
            lastPulled: null
        }
        this.toggleExpanded = this.toggleExpanded.bind(this);
        this.setVisibility = this.setVisibility.bind(this);
        this.handleNewUserMessage = this.handleNewUserMessage.bind(this);
        this.handleShowChat = this.handleShowChat.bind(this);
        this.handleMailIcon = this.handleMailIcon.bind(this);
        this.fetchFeedbackSettings = this.fetchFeedbackSettings.bind(this);
        this.handleFeedbackStatus = this.handleFeedbackStatus.bind(this);
        this.changeMailSetting = this.changeMailSetting.bind(this);
        this.fetchUnreadChat = this.fetchUnreadChat.bind(this);

    }

    //Ensure request is sent upon loading of component
    componentDidMount() {
        this.fetchFeedbackSettings();
        this.fetchFeedbackStatus();
        this.fetchUnreadChat();
    }

    handleNewUserMessage(newMessage) {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/feedback_chat', {
            header: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            },
            method: 'POST',
            body: JSON.stringify({
                feedback_id: that.props.feedbackId,
                user_id: sessionStorage.getItem('userId'),
                chat_text: newMessage,
                initiated_by_user: false
            })
        })
    }

    handleShowChat(e) {
        var that = this;

        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/chat_unread/feedback/' + that.props.feedbackId + '/user/' + sessionStorage.getItem('userId'), {
            header: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            },
            method: 'POST'
        }).then(result => that.props.onShowChat({
            showChat: true,
            index: this.props.feedbackId,
            unreadChat: [],
            title: that.props.title
        }));
    }

    toggleExpanded() {
        this.setState({expanded: !this.state.expanded});
    }

    //bug=661, function=662, generalfeedback=663
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

    handleVisibility() {
        var statusStyle = {
            color: 'black',
            //border: '1px solid #333333',
            //padding: '3px 7px',
            //background: 'linear-gradient(180deg, #fff, #ddd 40%, #ccc)',
            textAlign: 'center',
            fontSize: '12px'
        }
        if (this.props.visibility === false) {
            return <TiGroupOutline size={35} onClick={this.setVisibility} padding={30}/>;
        }
        if (this.props.visibility === true && this.props.published === true) {
            return <TiGroup size={35} padding={30}/>
        }
        if (this.props.visibility === true && this.props.published === false) {
            return <div><TiGroup color={'grey'} size={35} padding={30}/><br/>
                <div style={statusStyle}>Feedback under review</div>
            </div>
        }
    }

    setVisibility(e) {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/visibility/' + that.props.feedbackId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            },
            body: JSON.stringify({
                visible: true
            })
        }).then(result => that.props.update());
        e.stopPropagation();
    }

    handleMailIcon() {
        if (this.state.feedbackSetting === null || this.state.feedbackSetting.feedbackQuery === false) {
            return <MdEmail size={35} color='black' onClick={this.changeMailSetting}/>;
        }
        if (this.state.feedbackSetting.feedbackQueryChannel === "Email") {
            return <MdEmail size={35} color='green' onClick={this.changeMailSetting}/>;
        }
        else {
            return <MdEmail size={35} color='black' onClick={this.changeMailSetting}/>;
        }
    }

    fetchFeedbackSettings() {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/feedbacksettings/feedback/' + this.props.feedbackId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result => result.json())
            .then(result => {
                that.setState({feedbackSetting: result})
            });
    }

    changeMailSetting(e) {
        var that = this;
        var method = "";
        if (this.state.feedbackSetting === null || this.state.feedbackSetting.status === 404) {
            method = "POST";
        }
        else {
            method = "PUT";
        }
        var channel = "Feedback-To-Feedback Central";
        if (this.state.feedbackSetting.feedbackQueryChannel !== 'Email') {
            channel = "Email";
        }
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/feedbacksettings/', {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            },
            body: JSON.stringify({
                statusUpdates: true,
                statusUpdatesContactChannel: 'Email',
                feedbackQuery: true,
                feedbackQueryChannel: channel,
                feedback_id: that.props.feedbackId,
                globalFeedbackSetting: false
            })
        }).then(result => that.fetchFeedbackSettings());
        e.stopPropagation();
    }

    fetchFeedbackStatus() {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/status/feedback/' + this.props.feedbackId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result => result.json())
            .then(result => {
                that.setState({feedbackStatus: result})
            });
    }

    fetchUnreadChat() {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/chat_unread/user/' + sessionStorage.getItem('userId'), {
            method: 'GET',
            headers: {

                'Content-Type': 'application/json',
            }
        }).then(result => result.json())
            .then(result => {
                that.setState({unreadChat: result})
                console.log(`unread chat json: ${result}`);
                console.log(result);
            });
    }

    handleUnreadChat() {
        var that = this;
        var data = this.state.unreadChat;
        var content = "";

        if (data != null) {
            this.state.unreadChat.map(function (item, index) {
                if (item.hasOwnProperty("feedback") && item.feedback.hasOwnProperty("id") && item.feedback.id === that.props.feedbackId) {
                    console.log(`Juhu` + `feedbackID: ${item.feedback.id}`);
                    content = <label className={style.counts} size={35}>!</label>
                    return null;
                }
            });
        }

        return content;
    }

    handleFeedbackStatus() {
        //var that = this;
        if (this.state.feedbackStatus === null || this.state.feedbackStatus.status === null) {
            return <label className={style.statusunknown}>Status loading</label>
        }
        if (this.state.feedbackStatus.status === 'completed') {
            return <label className={style.statuscomplete}>Completed</label>
        }
        if (this.state.feedbackStatus.status === 'in_progress') {
            return <label className={style.statusprogress}>In Progress</label>
        }
        if (this.state.feedbackStatus.status === 'declined') {
            return <label className={style.statusdeclined}>Declined</label>
        }
        if (this.state.feedbackStatus.status === 'received') {
            return <label className={style.statusreceived}>Received</label>
        }
        else {
            return <label className={style.statusunknown}>No Status available</label>
        }
    }


    render() {
        var showChat = null;
        if (this.state.showChat) {
            showChat = <Widget title={this.props.title} subtitle="" handleNewUserMessage={this.handleNewUserMessage}/>
        }

        var dateText = "";
        var tmpDate = new Date(this.props.date.substring(0, this.props.date.indexOf('.')) + "Z");
        var options = {
            year: 'numeric',
            month: 'numeric',
            day: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
            second: 'numeric'
        }
        dateText = new Intl.DateTimeFormat('de-DE', options).format(tmpDate);

        return (<div style={{
            display: "flex",
            justifyContent: "space-around",
            background: 'linear-gradient(to top, #dfe9f3 0%, white 100%)'
        }}>
            <h5 align="left" style={{
                flexGrow: 2,
                fontSize: 12,
                fontStyle: 'italic'
            }}
                onClick={this.toggleExpanded}>{this.getIconForFeedbackType()}&nbsp; {(!this.state.expanded && this.props.title.length > 30) ? this.props.title.substring(0, 30) + "...more" : this.props.title}
                <div className={style.spacingstyle}>
                    <div align="left" style={{fontSize: 10, fontStyle: 'normal', fontWeight: 'normal'}}>sent
                        on {dateText}</div>
                    <div align="left" style={{fontSize: 10, color: '#169BDD', fontStyle: 'normal'}}>
                        Status: {this.handleFeedbackStatus()}</div>
                    <div align="left" style={{fontSize: 10, color: '#169BDD', fontStyle: 'normal'}}>Forum activity:
                        <FaThumbsOUp size={20} color={'black'} padding={10}/>
                        <span className={style.counts}>{this.props.likes}</span>
                        <FaThumbsODown size={20} color={'black'} padding={10}/>
                        <span className={style.counts}>{this.props.dislikes}</span>
                        <FaWechat size={20} color={'#63C050'} padding={10}/>
                        <span className={style.counts}>{this.props.commentnumber}</span>
                    </div>
                </div>
            </h5>
            {showChat}
            <div className={style.iconContainer}>

                {this.handleVisibility()}


                {this.handleMailIcon()}

                <div>
                    <FaWechat align="left" color={'#63C050'} style={{flexGrow: "1"}} onClick={this.handleShowChat}
                              size={35}/>
                    <span>{this.handleUnreadChat()}</span>
                </div>
            </div>
        </div>);
    }
}

export default FeedbackTitle;
