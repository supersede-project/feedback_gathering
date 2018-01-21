import React, {Component} from 'react';
/*import {
    Accordion,
    AccordionItem,
    AccordionItemTitle,
    AccordionItemBody
} from 'react-accessible-accordion';*/

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
            sorting: ''
        };
        this.handleShowCommentChange = this.handleShowCommentChange.bind(this);
    }

    componentDidMount() {
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
            console.log(item);
            cleanedResult.push(item);
          }
        })
        that.setState({data: cleanedResult});
      });
    }

    handleShowCommentChange(e) {
      this.setState({showComment: e.showComment, commentIndex: e.index});
    }

    onUpdate(sorting) { this.setState({sorting: sorting}); }

    render() {
        let content = null;
        if(!this.state.showComment)
        {
            let instance = this;
            content = <div><ForumSorting onUpdate={this.onUpdate.bind(this)} />
            <Accordion>
                {this.state.data.map(function (item, index) {
                  console.log(item);
                  if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
                  {
                    return (
                        <AccordionItem titleTag="span" title={<ForumTitle feedbackId={item.id} title={item.textFeedbacks[0].text} thumbsUp={item.likeCount} thumbsDown={item.dislikeCount} type={item.categoryFeedbacks[0].mechanismId} onShowCommentChange={instance.handleShowCommentChange} index={index}/>}>
                                <div>
                                    <ForumBody status="WIP" date={item.createdAt} onShowCommentChange={instance.handleShowCommentChange} index={index}/>
                                </div>
                        </AccordionItem>
                    )
                  }
                  return false;
                })}
            </Accordion></div>
        }
        else {

            content = <FeedbackForumCommentView post={this.state.data[this.state.commentIndex]}/>;

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
