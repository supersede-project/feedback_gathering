import React, {Component} from 'react';
import {
    Accordion,
    AccordionItem,
    AccordionItemTitle,
    AccordionItemBody
} from 'react-accessible-accordion';

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
            testData: [
                {
                    type: "Bug",
                    title: "Is it possible to display...",
                    status: "work in progress",
                    date: "25.09.2017 at 01:02 pm"
                },
                {
                    type: "Feature",
                    title: "Help text should be bigger...",
                    status: "work in progress",
                    date: "10.09.2017 at 06:22 pm"
                },
                {
                    type: "Response",
                    title: "I like your company...",
                    status: "Public",
                    date: "03.09.2017 at 10:31 am"
                }
            ],
            showComment: false,
            commentIndex: null,
            sorting: ''
        };
        this.handleShowCommentChange = this.handleShowCommentChange.bind(this);
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
                {this.state.testData.map(function (testItem, index) {
                    return (
                        <AccordionItem key={index}>
                            <AccordionItemTitle>
                                <ForumTitle title={testItem.title} type={testItem.type} onShowCommentChange={instance.handleShowCommentChange} index={index}/>
                            </AccordionItemTitle>
                            <AccordionItemBody>
                                <div>
                                    <ForumBody status={testItem.status} date={testItem.date} onShowCommentChange={instance.handleShowCommentChange} index={index}/>
                                </div>
                            </AccordionItemBody>
                        </AccordionItem>
                    )
                })}
            </Accordion></div>
        }
        else {

            content = <FeedbackForumCommentView post={this.state.testData[this.state.commentIndex]}/>;

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
