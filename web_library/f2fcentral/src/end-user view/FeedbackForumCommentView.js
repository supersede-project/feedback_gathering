import React, {Component} from 'react';
import CommentUI from '../helpers/CommentUI.js';
import ForumCommentTitle from '../helpers/ForumCommentTitle';

class FeedbackForumCommentView extends Component {

  render()
  {
    return (
        <div>
          <ForumCommentTitle className="forumComment" title={this.props.post.textFeedbacks[0].text} type={this.props.post.categoryFeedbacks[0].mechanismId} />
          <CommentUI feedbackId={this.props.post.id} backButtonSelected={this.props.backButtonSelected}/>
        </div>);
  }
}

export default FeedbackForumCommentView;
