import React, { Component } from 'react';
import CommentList from 'react-uikit-comment-list';
import Comment from 'react-uikit-comment';
import placeholder_avatar from './placeholder_avatar.svg'
import ForumCommentTitle from './ForumCommentTitle';

import style from './App.css';


class CommentUI extends Component {

  constructor(props) {
    super(props);
    this.state = {
      createComment : false,
      commentData : [],
      formValue: '',
      replyCommentId: 0,
    }
    this.createComment = this.createComment.bind(this);
    this.addComment = this.addComment.bind(this);
    this.handleTextAreaChange = this.handleTextAreaChange.bind(this);
    this.fetchData = this.fetchData.bind(this);
    this.setReplyCommentId = this.setReplyCommentId.bind(this);
    this.renderChildren = this.renderChildren.bind(this);
    this.backButtonPressed = this.backButtonPressed.bind(this);
    this.backButton = this.backButton.bind(this);
    this.getDeleteButton= this.getDeleteButton.bind(this);
  }

  componentDidMount() {
    this.fetchData();
  }

  fetchData() {
    var that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/comments/feedback/' + this.props.feedbackId, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      }
    }).then(result=>result.json())
    .then(result=> {
      that.setState({commentData: result, formValue: '', replyCommentId: 0, createComment: false});
    });
  }

    backButtonPressed(e){
        this.setState({createComment: false});
    }

    backButton(e){
        this.props.backButtonSelected();
    }

  createComment() {
    this.setState({createComment: true});
  }

  handleTextAreaChange(e) {
    this.setState({formValue: e.target.value});
  }

  addComment(e) {
    let comment = {
      feedback_id: this.props.feedbackId,
      user_id: sessionStorage.getItem('userId'),
      commentText: this.state.formValue,
      fk_parent_comment: this.state.replyCommentId,
      bool_is_developer: false,
      activeStatus: true,
      anonymous: false
    }
    let that = this;
    fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/comments/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': sessionStorage.getItem('token')
      },
      body: JSON.stringify(comment)
    }).then(result=>result.json())
    .then(result=> {
      that.fetchData();
    });
    e.preventDefault();
    e.stopPropagation();
  }

  setReplyCommentId(e, param) {
    this.setState({replyCommentId: param});
    e.preventDefault();
    e.stopPropagation();
  }

  renderChildren(comment) {
      var that = this;
    if(comment.children !== null && comment.children.length > 0)
      {
        return (<CommentList>
          {comment.children.map(function(childComment, index)
            {
              return (<Comment title={childComment.user.username} meta={new Date(childComment.createdAt.substring(0, childComment.createdAt.indexOf('.')) + "Z").toString()}
              avatar={{src: placeholder_avatar, alt: 'Avatar placeholder'}}>

              <p>
                {childComment.commentText}
              </p>
                  {that.getDeleteButton(childComment.id)}
            </Comment>)
          })}
        </CommentList>)
      }
      return;
  }

  getDeleteButton(commentId) {
      var that = this;
      if(window.adminUser) {
          return (<button className={style.formbutton1} type="button" onClick={(e) => that.deleteComment(commentId)}>Delete</button>);
      }
  }

  deleteComment(commentId) {
      fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/comments/' + commentId, {
      method: 'DELETE',
          headers: {
          'Content-Type': 'application/json',
          'Authorization': sessionStorage.getItem('token')
      }
  }).then(result => this.props.update());
      e.stopPropagation();
  }

  render() {

    let createCommentContent = null;
    let that = this;
    if(!this.state.createComment && this.state.replyCommentId == 0)
    {
      createCommentContent =
          <div>
          <button className={style.formbuttons1} type="button" onClick={this.createComment}>Add comment</button>&nbsp;
          <button className={style.formbuttons1} onClick={this.backButton}>Back</button>
      </div>
    }
    else {
      createCommentContent = <div className="createCommentContainer">
        <textarea value={this.state.formValue} rows="10" cols="80" onChange={this.handleTextAreaChange} placeholder="Insert your comment..."></textarea>
        <button className={style.formbuttons1} type="button" onClick={(e) => this.addComment(e)}>Submit comment</button>&nbsp;
        <button className={style.formbuttons1} type="button" onClick={this.backButtonPressed}>Back</button>
      </div>
    }

    return (
      <div>
        <CommentList>
          {this.state.commentData.map(function(comment, index) {
            if(comment.parentComment === null) {
            return (
              <Comment title={comment.user.username} meta={new Date(comment.createdAt.substring(0, comment.createdAt.indexOf('.')) + "Z").toString()}
                avatar={{src: placeholder_avatar, alt: 'Avatar placeholder'}}>

                <p>
                  {comment.commentText}
                </p>
                <button className={style.formbuttons1} type="button" onClick={(e) => that.setReplyCommentId(e, comment.id)}>Reply</button>
                  {that.getDeleteButton(comment.id)}

                  {that.renderChildren(comment)}
                </Comment>
              )}})}
            </CommentList>
            {createCommentContent}
          </div>
        );
      }
    }

    export default CommentUI;
