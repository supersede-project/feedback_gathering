import React, { Component } from 'react';
import CommentList from 'react-uikit-comment-list';
import Comment from 'react-uikit-comment';
import placeholder_avatar from '../placeholder_avatar.svg';
import style from '../css/App.css';


class ChatView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      lastPulled: null,
      data: [],
      formValue: '',
      userAvatars: []
    }
    this.fetchData = this.fetchData.bind(this);
    this.addChatEntry = this.addChatEntry.bind(this);
    this.fetchResponses = this.fetchResponses.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.backButtonPressed = this.backButtonPressed.bind(this);
    this.messageFromCurrentUser = this.messageFromCurrentUser.bind(this);
    this.handleAvatar = this.handleAvatar.bind(this);
  }

  componentDidMount() {
    this.fetchData();
  }

  fetchData() {
    var that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedback_chat/feedback/' + this.props.feedbackId, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      }
    }).then(result=>result.json())
    .then(result=> {
        result.sort((a, b) => {
          if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") < new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return -1;
          if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") > new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return 1;
          return 0;
        });
        that.setState({data: result, lastPulled: new Date()});
        that.handleAvatar();
    });
    setInterval(this.fetchResponses, 3000);
  }

  fetchResponses() {
    var that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedback_chat/feedback/' + this.props.feedbackId, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      }
    }).then(result=>result.json())
    .then(result=> {
        result.sort((a, b) => {
          if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") < new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return -1;
          if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") > new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return 1;
          return 0;
        })
        that.handleAvatar()
        that.setState({data: result, lastPulled: new Date()})
    })
  }

  addChatEntry() {
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
        chat_text: that.state.formValue,
        initiated_by_user: false
      })
    }).then(result => that.setState({formValue: ''}))
  }

  handleInputChange(e) {
    this.setState({formValue: e.target.value});
  }

  backButtonPressed(e) {
    this.props.onBackButtonPressed();
  }

  messageFromCurrentUser(id) {
    if(sessionStorage.getItem('userId') == parseInt(id)) {
      return style.usermessage;
    }
    return style.oppositionmessage;
  }

  handleAvatar(){
    var that = this;
    var userIdList = [];
    //src: placeholder_avatar, alt: 'Avatar placeholder'}
      this.state.data.map((item, index) => {
          if(!userIdList.includes(item.user.id)) {
              userIdList.push(item.user.id);
          }
      });

      userIdList.forEach((el) => {
          fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/getImage/user/' + el, {
              method: 'GET',
              headers: {
                  'Content-Type': 'image/png',
                  'Authorization': sessionStorage.getItem('token')
              }
          }).then(result => result.blob()).then(result => {
              var item = that.state.userAvatars.filter((item) => item.userId == el);

              that.state.userAvatars.push({userId: el, avatar: URL.createObjectURL(result)})
          });
      });
  }

  render() {
    let that = this;
    return (
      <div>
        <h4 className={style.companyfeedbacktext}>{that.props.title}</h4>
        <CommentList>
          {this.state.data.map(function(item, index) {
            var userId = item.user.id;
            var userAvatars = that.state.userAvatars.filter(el => el.userId === userId);
            var avatar = '';

              var dateText = "";
              var tmpDate = new Date(item.chatDate.substring(0, item.chatDate.indexOf('.')) + "Z");
              var options = {
                  year: 'numeric',
                  month: 'numeric',
                  day: 'numeric',
                  hour: 'numeric',
                  minute: 'numeric',
                  second: 'numeric'
              }
              dateText = new Intl.DateTimeFormat('de-DE', options).format(tmpDate);

            if(userAvatars.length > 0) {
                avatar = userAvatars[0].avatar;
            }
            return (<Comment className={that.messageFromCurrentUser(userId)} meta={dateText} avatar={{src: avatar}}>
              <p>
                {item.chatText}
              </p>
            </Comment>)
          })}
        </CommentList>
        <div className="createCommentContainer">
          <textarea value={this.state.formValue} type="text" rows="4" cols="50" onChange={this.handleInputChange} placeholder="Please enter your response here..."/>
        </div>
        <button className={style.formbuttons1} type="button" onClick={(e) => this.addChatEntry()}>Submit</button>&nbsp;
        <button className={style.formbuttons1} type="button" onClick={this.backButtonPressed}>Back</button>
      </div>
    );
  }
}
export default ChatView;
