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
import FeedbackTitle from './FeedbackTitle';
import FeedbackBody from './FeedbackBody';
import FeedbackData from './FeedbackData';


class MyFeedbacksTabAccordion extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        let data = new FeedbackData().getFeedbackData();
        return (
            <div className="MyFeedbacksTabAccordion">
                <div className="MyFeedback">
                    <Accordion>
                        {data.map(function (item, index) {
                            return (
                                <AccordionItem key={index}>
                                    <AccordionItemTitle>
                                        <FeedbackTitle type={item.categoryFeedbacks[0].mechanismId} title={item.categoryFeedbacks[0].text}/>
                                    </AccordionItemTitle>
                                    <AccordionItemBody>
                                        <FeedbackBody date={item.createdAt} status={item.status}/>
                                    </AccordionItemBody>
                                </AccordionItem>
                            )
                        })}
                    </Accordion>
                </div>
            </div>
        );
    }
}

export default MyFeedbacksTabAccordion;
