import React, {Component} from 'react';
import CommentList from 'react-uikit-comment-list';
import Comment from 'react-uikit-comment';
import placeholder_avatar from '../placeholder_avatar.svg'
import ForumCommentTitle from './ForumCommentTitle';

import style from '../css/App.css';


class CommentUI extends Component {

    constructor(props) {
        super(props);
        this.state = {
            createComment: false,
            commentData: [],
            formValue: '',
            replyCommentId: 0,
            userAvatars: []
        }
        this.createComment = this.createComment.bind(this);
        this.addComment = this.addComment.bind(this);
        this.handleTextAreaChange = this.handleTextAreaChange.bind(this);
        this.fetchData = this.fetchData.bind(this);
        this.setReplyCommentId = this.setReplyCommentId.bind(this);
        this.renderChildren = this.renderChildren.bind(this);
        this.backButtonPressed = this.backButtonPressed.bind(this);
        this.backButton = this.backButton.bind(this);
        this.getDeleteButton = this.getDeleteButton.bind(this);
        this.getAvatar = this.getAvatar.bind(this);
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/comments/feedback/' + this.props.feedbackId, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result => result.json())
            .then(result => {
                that.setState({commentData: result, formValue: '', replyCommentId: 0, createComment: false});
                that.getAvatar()

            });
    }

    backButtonPressed(e) {
        this.setState({createComment: false});
    }

    backButton(e) {
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
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/comments/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            },
            body: JSON.stringify(comment)
        }).then(result => result.json())
            .then(result => {
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
        if (comment.children !== null && comment.children.length > 0) {
            return (<CommentList>
                {comment.children.map(function (childComment, index) {
                    var userId = comment.user.id;
                    var userAvatars = that.state.userAvatars.filter(el => el.userId === userId);
                    var avatar = '';

                    var dateText = "";
                    var tmpDate = new Date(childComment.createdAt.substring(0, childComment.createdAt.indexOf('.')) + "Z");
                    var options = {
                        year: 'numeric',
                        month: 'numeric',
                        day: 'numeric',
                        hour: 'numeric',
                        minute: 'numeric',
                        second: 'numeric'
                    }
                    dateText = new Intl.DateTimeFormat('de-DE', options).format(tmpDate);

                    if (userAvatars.length > 0) {
                        avatar = userAvatars[0].avatar;
                    }
                    return (<Comment title={childComment.user.username}
                                     meta={dateText}
                                     avatar={{src: avatar}}>

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
        if (window.adminUser) {
            return (<button className={style.formbutton1} type="button" onClick={(e) => that.deleteComment(commentId)}>
                Delete</button>);
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

    getAvatar() {
        var that = this;
        var userIdList = [];

        that.state.commentData.map((item, index) => {
            console.log('commentData', item.user.id);
            if (!userIdList.includes(item.user.id)) {
                userIdList.push(item.user.id);
            }
        });

        var promises = [];
        userIdList.forEach((el) => {
            promises.push(fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/getImage/user/' + el, {
                method: 'GET',
                headers: {
                    'Content-Type': 'image/png',
                    'Authorization': sessionStorage.getItem('token')
                }
            }).then(result => result.blob()))
        });

        Promise.all(promises).then(results => {
            var userAvatars = [];
            var i;
            for (i = 0; i < userIdList.length; i++) {
                userAvatars.push({userId: userIdList[i], avatar: URL.createObjectURL(results[i])});
            }

            that.setState({userAvatars: userAvatars});
            console.log('userAvatar', that.state.userAvatars);
        });
    }


    render() {

        let createCommentContent = null;
        let that = this;
        if (!this.state.createComment && this.state.replyCommentId == 0) {
            createCommentContent =
                <div>
                    <button className={style.formbuttons1} type="button" onClick={this.createComment}>Add comment
                    </button>
                    &nbsp;
                    <button className={style.formbuttons1} onClick={this.backButton}>Back</button>
                </div>
        }
        else {
            createCommentContent = <div className="createCommentContainer">
            <textarea value={this.state.formValue} rows="10" cols="80" onChange={this.handleTextAreaChange}
                      placeholder="Insert your comment..."></textarea>
                <button className={style.formbuttons1} type="button" onClick={(e) => this.addComment(e)}>Submit comment
                </button>
                &nbsp;
                <button className={style.formbuttons1} type="button" onClick={this.backButtonPressed}>Back</button>
            </div>
        }

        return (
            <div>
                <CommentList>
                    {this.state.commentData.map(function (comment, index) {
                        var userId = comment.user.id;
                        var userAvatars = that.state.userAvatars.filter(el => el.userId === userId);
                        var avatar = '';
                        console.log(userAvatars.length);

                        var dateText = "";
                        var tmpDate = new Date(comment.createdAt.substring(0, comment.createdAt.indexOf('.')) + "Z");
                        var options = {
                            year: 'numeric',
                            month: 'numeric',
                            day: 'numeric',
                            hour: 'numeric',
                            minute: 'numeric',
                            second: 'numeric'
                        }
                        dateText = new Intl.DateTimeFormat('de-DE', options).format(tmpDate);

                        if (userAvatars.length > 0) {

                            avatar = userAvatars[0].avatar;
                        }
                        if (comment.parentComment === null) {
                            return (
                                <Comment title={comment.user.username}
                                         meta={dateText}
                                         avatar={{src: avatar}}>
                                    <p>
                                        {comment.commentText}
                                    </p>
                                    <button className={style.formbuttons1} type="button"
                                            onClick={(e) => that.setReplyCommentId(e, comment.id)}>Reply
                                    </button>
                                    {that.getDeleteButton(comment.id)}

                                    {that.renderChildren(comment)}
                                </Comment>
                            )
                        }
                    })}
                </CommentList>
                {createCommentContent}
            </div>
        );
    }
}

export default CommentUI;
