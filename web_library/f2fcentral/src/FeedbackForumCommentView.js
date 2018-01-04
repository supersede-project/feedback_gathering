import React, {Component} from 'react';
import CommentUI from './CommentUI.js';
import ForumCommentTitle from './ForumCommentTitle';

class FeedbackForumCommentView extends Component {

  render()
  {
    return (<div><ForumCommentTitle className="forumComment" title={this.props.post.title} type={this.props.post.type} /><CommentUI/></div>);
  }
}

export default FeedbackForumCommentView;
