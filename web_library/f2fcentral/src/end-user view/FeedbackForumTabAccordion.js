import React, {Component} from 'react';
/*import {
    Accordion,
    AccordionItem,
    AccordionItemTitle,
    AccordionItemBody
} from 'react-accessible-accordion';*/
import {PulseLoader} from 'react-spinners';
import {Accordion, AccordionItem} from 'react-sanfona';

import style from '../css/App.css';
import 'react-tabs/style/react-tabs.css';

import 'react-accessible-accordion/dist/react-accessible-accordion.css';
import ForumTitle from "./ForumTitle";
import ForumBody from "./ForumBody";
import FeedbackForumCommentView from "./FeedbackForumCommentView";
import ForumSorting from "../helpers/ForumSorting";
import CompanyFeedback from "../helpers/CompanyFeedback";


class FeedbackForumTabAccordion extends Component {

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            unsortedData: [],
            showComment: false,
            commentIndex: null,
            sorting: 'date1',
            loading: true,
            isChecked: true,
            unreadFeedbacks: []
        };
        this.handleShowCommentChange = this.handleShowCommentChange.bind(this);
        this.fetchData = this.fetchData.bind(this);
        this.handleBackButton = this.handleBackButton.bind(this);
        this.handleSelection = this.handleSelection.bind(this);
        this.fetchUnreadFeedback = this.fetchUnreadFeedback.bind(this);
    }

    componentDidMount() {
        this.fetchData(null);
    }

    fetchData(e) {
        var that = this;
        var promises = [];
        promises.push(fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result => result.json()));

        promises.push(fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/feedback_company', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result => result.json()));

        Promise.all(promises)
            .then(resultArray => {
                var cleanedResult = [];
                resultArray.forEach(result => {
                    result.map(function (item, index) {
                        if (!item.hasOwnProperty("published") || item.published || item.visibility) {
                            cleanedResult.push(item);
                        }

                    });
                });
                that.setState({data: cleanedResult, unsortedData: cleanedResult, loading: false});
                this.fetchUnreadFeedback();
                this.fetchStatus();
            });
    }


    fetchStatus() {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/status', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result => result.json())
            .then(result => {
                result.forEach(status => {
                    var index = that.state.data.findIndex(el => el.id === status.feedback.id);
                    if (index !== -1) {
                        that.state.data[index].status = status;
                    }
                });
            });
    }

    handleShowCommentChange(e) {
        this.setState({showComment: e.showComment, commentIndex: e.index});
    }

    handleBackButton() {
        this.setState({showComment: false, commentIndex: null});
    }

    handleSelection() {
        this.setState((previousState) => {
            return {isChecked: !previousState.isChecked}
        });
    }

    fetchUnreadFeedback() {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/get_published/unread/user/' + sessionStorage.getItem('userId'), {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        })
            .then(result => result.json())
            .then(result => that.setState({unreadFeedbacks: result}));
    }

    onUpdate(sorting) {
        var data = this.state.unsortedData;
        switch (this.state.sorting) {
            case "date1":
                data = [].concat(this.state.unsortedData).sort(function (a, b) {
                    return new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z") - new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z");
                });
                break;
            case "date2":
                data = [].concat(this.state.unsortedData).sort(function (a, b) {
                    if (new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z") < new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z")) return -1;
                    if (new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z") > new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z")) return 1;
                    return 0;
                });
                break;
            case "myfeedback":
                data = [].concat(this.state.unsortedData).filter(element => {
                        if(!element.hasOwnProperty("userIdentification")){
                            return false;
                        }
                        return element.userIdentification === parseInt(sessionStorage.getItem('userId'));
                });

                /* sort(function (a, b) {
                  if (a.userIdentification === sessionStorage.getItem('userId')) return 1;
                  if (b.userIdentification === sessionStorage.getItem('userId')) return -1;
                  return 0;
                });*/
                break;
            case "mostlike":
                data = [].concat(this.state.unsortedData).sort(function (a, b) {
                    if (a.likeCount > b.likeCount) return -1;
                    if (b.likeCount > a.likeCount) return 1;
                    return 0;
                });
                break;
            case "unrated":
                data = [].concat(this.state.unsortedData).filter(element => {
                    if(!element.hasOwnProperty("ratingFeedbacks")){
                        return element;
                    }
                    return element.ratingFeedbacks.length == 0;
                });

                    /*sort(function (a, b) {
                        if (a.ratingFeedbacks.length == 0) return -1;
                        if (b.ratingFeedbacks.length == 0) return 1;
                        return 0;
                    });*/


                break;
            case "received":
                data = [].concat(this.state.unsortedData).filter(element => {
                    if(!element.hasOwnProperty("status")) {
                        return false;
                    }
                    return element.status.status === 'received';
                });
                break;
            case "progress":
                data = [].concat(this.state.unsortedData).filter(element => {
                    if (!element.hasOwnProperty("status")) {
                        return false;
                    }
                    return element.status.status === 'in_progress';
                });
                break;
            case "declined":
                data = [].concat(this.state.unsortedData).filter(element => {
                    if (!element.hasOwnProperty("status")) {
                        return false;
                    }
                    return element.status.status === 'declined';
                });
                break;
            case "completed":
                data = [].concat(this.state.unsortedData).filter(element => {
                    if (!element.hasOwnProperty("status")) {
                        return false;
                    }
                    return element.status.status === 'completed';
                });
                break;
            case "company":
                data = [].concat(this.state.unsortedData).filter(element => {
                    if (element.hasOwnProperty("promote")) {
                        return element;
                    }
                });
                break;

            default:
                data = [].concat(this.state.unsortedData).sort(function (a, b) {
                    return new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z") - new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z");
                });
        }
        this.setState({sorting: sorting, data: data});
    }

    sortData() {
        switch (this.state.sorting) {
            case "date1":
                return [].concat(this.state.unsortedData).sort(function (a, b) {
                    return new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z") - new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z");
                });
                break;
            case "date2":
                return [].concat(this.state.unsortedData).sort(function (a, b) {
                    if (new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z") < new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z")) return -1;
                    if (new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z") > new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z")) return 1;
                    return 0;
                });
                break;
            case "myfeedback":

                return [].concat(this.state.unsortedData).filter(element => {
                    if(!element.hasOwnProperty("userIdentification")){
                        return false;
                    }
                    return element.userIdentification === parseInt(sessionStorage.getItem('userId'));
                });
                /*return [].concat(this.state.unsortedData).sort(function (a, b) {
                    if (a.userIdentification === sessionStorage.getItem('userId')) return 1;
                    if (b.userIdentification === sessionStorage.getItem('userId')) return -1;
                    return 0;
                });*/
                break;
            case "mostlike":
                return [].concat(this.state.unsortedData).sort(function (a, b) {
                    if (a.likeCount > b.likeCount) return -1;
                    if (b.likeCount > a.likeCount) return 1;
                    return 0;
                });
                break;
            case "unrated":
                return [].concat(this.state.unsortedData).filter(element => {
                    if(!element.hasOwnProperty("ratingFeedbacks")){
                        return element;
                    }
                    return element.ratingFeedbacks.length == 0;
                });
                /*return [].concat(this.state.unsortedData).sort(function (a, b) {
                    if (a.ratingFeedbacks.length == 0) return -1;
                    if (b.ratingFeedbacks.length == 0) return 1;
                    return 0;
                });*/
                break;
            case "received":
                return [].concat(this.state.unsortedData).filter(element => {
                    if(!element.hasOwnProperty("status")) {
                        return false;
                    }
                    return element.status.status === 'received';
                });
                break;
            case "progress":
                return [].concat(this.state.unsortedData).filter(element => {
                    if (!element.hasOwnProperty("status")) {
                        return false;
                    }
                    return element.status.status === 'in_progress';
                });
                break;
            case "declined":
                return [].concat(this.state.unsortedData).filter(element => {
                    if (!element.hasOwnProperty("status")) {
                        return false;
                    }
                    return element.status.status === 'declined';
                });
                break;
            case "completed":
                return [].concat(this.state.unsortedData).filter(element => {
                    if (!element.hasOwnProperty("status")) {
                        return false;
                    }
                    return element.status.status === 'completed';
                });
                break;
            case "company":
                return [].concat(this.state.unsortedData).filter(element => {
                    if (element.hasOwnProperty("promote")) {
                        return element;
                    }
                });
                break;
            default:
                return [].concat(this.state.unsortedData).sort(function (a, b) {
                    return new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z") - new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z");
                });
        }
    }

    render() {
        let content = null;
        let instance = this;
        if (!this.state.showComment) {
            let data = this.state.data;
            let sortedData = this.sortData();
            content = <div>
                <ForumSorting onUpdate={this.onUpdate.bind(this)}/>
                <Accordion>
                    {sortedData.map(function (item, index) {
                        //console.log(sortedData);
                        if (!item.hasOwnProperty("textFeedbacks") || (item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)) {
                            var unread = false;
                            if (instance.state.unreadFeedbacks.filter((element) => element.id == item.id).length > 0) {
                                unread = true;
                            }
                            var visibility = false;
                            if (item.visibility === true && item.published === false) {
                                visibility = true;
                            }
                            var blocked = false;
                            if(item.blocked === true && item.visibility === true && item.published === true){
                                blocked = true;
                            }

                            var accordionItemTitle = '';
                            if (!item.hasOwnProperty("textFeedbacks")) {
                                accordionItemTitle = <CompanyFeedback title={item.text}/>;
                            }
                            else {
                                accordionItemTitle =
                                    <ForumTitle feedbackId={item.id} unread={unread} visibility={visibility}
                                                title={item.textFeedbacks[0].text} thumbsUp={item.likeCount}
                                                thumbsDown={item.dislikeCount} comment={item.commentCount}
                                                type={item.categoryFeedbacks[0].parameterId}
                                                onShowCommentChange={instance.handleShowCommentChange}
                                                update={instance.fetchData.bind(instance)} index={index}/>;
                            }
                            return (
                                <AccordionItem className={style.reactsanfonaitem} titleTag="span"
                                               title={accordionItemTitle}>
                                    {/*<div>
                                    <ForumBody status="WIP" date={item.createdAt} onShowCommentChange={instance.handleShowCommentChange} index={index}/>
                                </div>*/}
                                </AccordionItem>
                            )
                        }
                        return false;
                    })}
                </Accordion>
            </div>
        }
        else if (this.state.loading) {
            var divStyle = {
                position: 'absolute',
                top: '50%',
                left: '50%',
                marginRight: '-50%',
                transform: 'translate(-50%, -50%)'
            }
            content = <div style={divStyle}><PulseLoader
                loading={this.state.loading}
            /></div>
        }
        else {

            content = <FeedbackForumCommentView backButtonSelected={this.handleBackButton}
                                                post={this.state.data.find(function (a) {
                                                    return a.id === instance.state.commentIndex;
                                                })}/>;

        }

        return (
            <div className={style.MyFeedbacksTabAccordion}>
                <div className={style.MyFeedback}>
                    {content}
                </div>
            </div>
        );
    }
}

export default FeedbackForumTabAccordion;
