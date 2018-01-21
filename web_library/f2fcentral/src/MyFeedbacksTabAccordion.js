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
import FeedbackTitle from './FeedbackTitle';
import FeedbackBody from './FeedbackBody';
import FeedbackData from './FeedbackData';


class MyFeedbacksTabAccordion extends Component {

    constructor(props) {
        super(props);
        this.state = {
          data : []
        }
    }

    componentDidMount() {
      var that = this;
      fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/user_identification/' + sessionStorage.getItem('userId'), {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': sessionStorage.getItem('token')
          }
      }).then(result=>result.json())
      .then(result=> {
        that.setState({data: result})
      });
    }

    render() {
      let toRender = null;
      var that = this;
      if(that.state.data.length > 0) {
        /*toRender = <Accordion>
            {that.state.data.map(function (item, index) {
                console.log(item);
                if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
                {
                  return (
                      <AccordionItem key={index}>
                          <AccordionItemTitle>
                              <FeedbackTitle type={item.categoryFeedbacks[0].mechanismId} title={item.textFeedbacks[0].text}/>
                          </AccordionItemTitle>
                          <AccordionItemBody>
                              <FeedbackBody date={item.createdAt} status="WIP"/>
                          </AccordionItemBody>
                      </AccordionItem>
                  )
                }
                return false;
            })}
        </Accordion>;*/

        /*toRender = this.state.data.map(function(item, index) {
          console.log(item);
          if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
          {
            return (
              <li>
              <div className="accordion">{item.textFeedbacks[0].text}</div>
              <div className="accordion-content"></div>
              </li>
            )
          }
          return false;
        })*/
        toRender = <Accordion>
        {that.state.data.map(function (item, index) {
            if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
            {
              return (
                  <AccordionItem titleTag="span" title={<FeedbackTitle type={item.categoryFeedbacks[0].mechanismId} title={item.textFeedbacks[0].text}/>}>
                  </AccordionItem>
              )
            }
            return false;
        })}
                  </Accordion>;
      }
      else {
        toRender = <p>No Elements to show</p>;
      }
        return (
            <div className="MyFeedbacksTabAccordion">
                <div className="MyFeedback">
                <ul>
                    {toRender}
                    </ul>
                </div>
            </div>
        );
    }
}

export default MyFeedbacksTabAccordion;
