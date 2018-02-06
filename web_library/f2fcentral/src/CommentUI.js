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
        testCommentData : [],
        formValue: ''
      }
      this.createComment = this.createComment.bind(this);
      this.addComment = this.addComment.bind(this);
      this.handleTextAreaChange = this.handleTextAreaChange.bind(this);
    }

    createComment() {
      this.setState({createComment: true});
    }

    handleTextAreaChange(e) {
      this.setState({formValue: e.target.value});
    }

    addComment(e) {
      let comment = {
        author: "Author",
        comment: this.state.formValue
      }
      let testCommentData = this.state.testCommentData;
      testCommentData.push(comment);
      this.setState({testCommentData: testCommentData, formValue: ''})
      e.preventDefault();
      e.stopPropagation();
    }

    render() {

        let createCommentContent = null;
        if(!this.state.createComment)
        {
            createCommentContent = <button type="button" onClick={this.createComment}>Add comment</button>
        }
        else {
          createCommentContent = <div className="createCommentContainer">
          <textarea value={this.state.formValue} rows="10" cols="80" onChange={this.handleTextAreaChange} placeholder="Insert your comment..."></textarea>
          <button type="button" onClick={this.addComment}>Submit comment</button>
          </div>
        }

        return (
          <div>
            <CommentList>
              {this.state.testCommentData.map(function(comment, index) {
                return (<Comment title={comment.author} meta='12 days ago | Profile | #'
                         avatar={{src: placeholder_avatar, alt: 'Avatar placeholder'}}>

                    <p>
                        {comment.comment}
                    </p>
                    </Comment>
              )})}
            </CommentList>
            {createCommentContent}
            </div>
        );
    }
}

export default CommentUI;
