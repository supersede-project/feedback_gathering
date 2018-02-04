import React, { Component } from 'react';
import CommentList from 'react-uikit-comment-list';
import Comment from 'react-uikit-comment';
import placeholder_avatar from './placeholder_avatar.svg'
import ForumCommentTitle from './ForumCommentTitle';

import './App.css';


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
  }

  componentDidMount() {
    this.fetchData();
    setInterval(this.fetchData, 5000);
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
            </Comment>)
          })}
        </CommentList>)
      }
      return;
  }

  render() {

    let createCommentContent = null;
    let that = this;
    if(!this.state.createComment && this.state.replyCommentId == 0)
    {
      createCommentContent = <button type="button" onClick={this.createComment}>Add comment</button>
    }
    else {
      createCommentContent = <div className="createCommentContainer">
        <textarea value={this.state.formValue} rows="10" cols="80" onChange={this.handleTextAreaChange} placeholder="Insert your comment..."></textarea>
        <button type="button" onClick={(e) => this.addComment(e)}>Submit comment</button>
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
                <button type="button" onClick={(e) => that.setReplyCommentId(e, comment.id)}>Reply</button>
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
