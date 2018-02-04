import React, {Component} from 'react';
import CommentUI from './CommentUI.js';
import ForumCommentTitle from './ForumCommentTitle';

class FeedbackForumCommentView extends Component {

  render()
  {
    return (<div><ForumCommentTitle className="forumComment" title={this.props.post.textFeedbacks[0].text} type={this.props.post.categoryFeedbacks[0].mechanismId} /><CommentUI feedbackId={this.props.post.id}/></div>);
  }
}

export default FeedbackForumCommentView;
