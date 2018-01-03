import React, {Component} from 'react';

class ForumBody extends Component {

  constructor(props) {
    super(props);
    this.handleShowCommentChange = this.handleShowCommentChange.bind(this);
  }

  handleShowCommentChange(e) {
    this.props.onShowCommentChange({showComment: true, index: this.props.index});
  }

    render()
    {
        return (<div><div align="left" style={{fontSize: 10}}>sent on {this.props.date}</div>
            <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Status: {this.props.status}</div>
            <div align="left" className="forum-body-show-comment-container"><button type="button" onClick={this.handleShowCommentChange}>Show comments</button></div></div>);
    }
}

export default ForumBody;
