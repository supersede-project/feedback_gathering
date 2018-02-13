import React, {Component} from 'react';
/*import {
    Accordion,
    AccordionItem,
    AccordionItemTitle,
    AccordionItemBody
} from 'react-accessible-accordion';*/
import { PulseLoader } from 'react-spinners';
import { Accordion, AccordionItem } from 'react-sanfona';

import './App.css';
import 'react-tabs/style/react-tabs.css';

import 'react-accessible-accordion/dist/react-accessible-accordion.css';
import ForumTitle from "./ForumTitle";
import ForumBody from "./ForumBody";
import FeedbackForumCommentView from "./FeedbackForumCommentView";
import ForumSorting from "./ForumSorting";


class FeedbackForumTabAccordion extends Component {

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            showComment: false,
            commentIndex: null,
            sorting: 'date1',
            loading: true
        };
        this.handleShowCommentChange = this.handleShowCommentChange.bind(this);
        this.fetchData = this.fetchData.bind(this);
        this.handleBackButton = this.handleBackButton.bind(this);
    }

    componentDidMount() {
      this.fetchData(null);
    }

    fetchData(e) {
      var that = this;
      fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks', {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': sessionStorage.getItem('token')
          }
      }).then(result=>result.json())
      .then(result=> {
        var cleanedResult = [];
        result.map(function(item, index) {
          if(item.visibility) {
            cleanedResult.push(item);
          }
        })
        that.setState({data: cleanedResult, loading: false});
      });
    }

    handleShowCommentChange(e) {
      this.setState({showComment: e.showComment, commentIndex: e.index});
    }

    handleBackButton(){
        this.setState({ showComment: false, commentIndex: null});
    }

    onUpdate(sorting) {
      var data = this.state.data;
      switch(this.state.sorting) {
        case "date1":
        data = [].concat(this.state.data).sort(function (a, b){
          return new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z") - new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z");
        });
        break;
        case "date2":
        data = [].concat(this.state.data).sort(function (a, b) {
          if (new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z") < new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z")) return -1;
          if (new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z") > new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z")) return 1;
          return 0;
        });
        break;
        case "myfeedback":
        data = [].concat(this.state.data).sort(function (a, b) {
          if(a.userIdentification === sessionStorage.getItem('userId')) return 1;
          if(b.userIdentification === sessionStorage.getItem('userId')) return -1;
          return 1;
        });
        break;
        case "mostlike":
        data = [].concat(this.state.data).sort(function (a, b) {
          if(a.likeCount > b.likeCount) return -1;
          if(b.likeCount > a.likeCount) return 1;
          return 0;
        });
        break;
        case "unrated":
        data = [].concat(this.state.data).sort(function(a,b) {
          if(a.ratingFeedbacks.length == 0) return -1;
          if(b.ratingFeedbacks.length == 0) return 1;
          return 0;
        });
        break;
        default:
      }
      this.setState({sorting: sorting, data: data});
    }

    sortData() {
      switch(this.state.sorting) {
        case "date1":
        return [].concat(this.state.data).sort(function (a, b){
          return new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z") - new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z");
        });
        break;
        case "date2":
        return [].concat(this.state.data).sort(function (a, b) {
          if (new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z") < new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z")) return -1;
          if (new Date(a.createdAt.substring(0, a.createdAt.indexOf('.')) + "Z") > new Date(b.createdAt.substring(0, b.createdAt.indexOf('.')) + "Z")) return 1;
          return 0;
        });
        break;
        case "myfeedback":
        return [].concat(this.state.data).sort(function (a, b) {
          if(a.userIdentification === sessionStorage.getItem('userId')) return 1;
          if(b.userIdentification === sessionStorage.getItem('userId')) return -1;
          return 1;
        });
        break;
        case "mostlike":
        return [].concat(this.state.data).sort(function (a, b) {
          if(a.likeCount > b.likeCount) return -1;
          if(b.likeCount > a.likeCount) return 1;
          return 0;
        });
        break;
        case "unrated":
        return [].concat(this.state.data).sort(function(a,b) {
          if(a.ratingFeedbacks.length == 0) return -1;
          if(b.ratingFeedbacks.length == 0) return 1;
          return 0;
        });
        break;
        default:
      }
    }

    render() {
        let content = null;
        let instance = this;
        if(!this.state.showComment)
        {
            let data = this.state.data;
            let sortedData = this.sortData();
            content = <div>
            <ForumSorting onUpdate={this.onUpdate.bind(this)} />
            <Accordion>
                {sortedData.map(function (item, index) {
                  if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
                  {
                    return (
                        <AccordionItem titleTag="span" title={<ForumTitle feedbackId={item.id} title={item.textFeedbacks[0].text} thumbsUp={item.likeCount} thumbsDown={item.dislikeCount} comment={item.commentCount} type={item.categoryFeedbacks[0].parameterId} onShowCommentChange={instance.handleShowCommentChange} update={instance.fetchData.bind(instance)} index={index}/>}>
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

            content = <FeedbackForumCommentView backButtonSelected={this.handleBackButton} post={this.state.data.find(function(a)
            {
              return a.id === instance.state.commentIndex;
            })}/>;

        }

        return (
            <div className="MyFeedbacksTabAccordion">
                <div className="MyFeedback">
                    {content}
                </div>
            </div>
        );
    }
}

export default FeedbackForumTabAccordion;
