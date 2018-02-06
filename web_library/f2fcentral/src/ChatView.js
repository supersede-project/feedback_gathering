import React, { Component } from 'react';
import CommentList from 'react-uikit-comment-list';
import Comment from 'react-uikit-comment';
import placeholder_avatar from './placeholder_avatar.svg';
import style from './App.css';


class ChatView extends Component {

  constructor(props) {
    super(props);
    this.state = {
      lastPulled: null,
      data: [],
      formValue: '',
    }
    this.fetchData = this.fetchData.bind(this);
    this.addChatEntry = this.addChatEntry.bind(this);
    this.fetchResponses = this.fetchResponses.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.backButtonPressed = this.backButtonPressed.bind(this);
    this.messageFromCurrentUser = this.messageFromCurrentUser.bind(this);
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
      console.log("RESULT");
      console.log(result);

        result.sort((a, b) => {
          if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") < new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return -1;
          if (new Date(a.chatDate.substring(0, a.chatDate.indexOf('.')) + "Z") > new Date(b.chatDate.substring(0, b.chatDate.indexOf('.')) + "Z")) return 1;
          return 0;
        });
        that.setState({data: result, lastPulled: new Date()});
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
    return '';
  }

  render() {
    let that = this;
    console.log(this.state.data);
    return (
      <div>
        <h3>{that.props.title}</h3>
        <CommentList>
          {this.state.data.map(function(item, index) {
            console.log(item);
            var userId = item.user.id;
            return (<Comment className={that.messageFromCurrentUser(userId)} meta={new Date(item.chatDate.substring(0, item.chatDate.indexOf('.')) + "Z").toString()}
              avatar={{src: placeholder_avatar, alt: 'Avatar placeholder'}}>
              <p>
                {item.chatText}
              </p>
            </Comment>)
          })}
        </CommentList>
        <div className="createCommentContainer">
          <input value={this.state.formValue} type="text" onChange={this.handleInputChange} placeholder="Type message..."/>
          <button type="button" onClick={(e) => this.addChatEntry()}>Send</button>
        </div>
        <button type="button" onClick={this.backButtonPressed}>Back</button>
      </div>
    );
  }
}
export default ChatView;
