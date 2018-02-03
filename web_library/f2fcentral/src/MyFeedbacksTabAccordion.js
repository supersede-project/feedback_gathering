import React, {Component} from 'react';
import { PulseLoader } from 'react-spinners';

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
          data : [],
          loading: true,
        }
        this.fetchData = this.fetchData.bind(this);
    }

    componentDidMount() {
      this.fetchData(null);
    }

    fetchData(e) {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/user_identification/' + sessionStorage.getItem('userId'), {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result=>result.json())
            .then(result=> {
                that.setState({data: result, loading: false})
            });
    }

    render() {
      let toRender = null;
      var that = this;
      if(that.state.data.length > 0) {
        toRender = <Accordion>
        {that.state.data.map(function (item, index) {
            if(item.textFeedbacks.length > 0 && item.categoryFeedbacks.length > 0)
            {
              console.log(item);
              return (
                  <AccordionItem titleTag="span" title={<FeedbackTitle feedbackId={item.id} update={that.fetchData} type={item.categoryFeedbacks[0].parameterId} title={item.textFeedbacks[0].text} visibility={item.visibility} date={item.createdAt} status="WIP"/>}>
                  </AccordionItem>
              )
            }
            return false;
        })}
                  </Accordion>;
      }
      else if (this.state.loading) {
        var divStyle = {
          position: 'absolute',
          top: '50%',
          left: '50%',
          marginRight: '-50%',
          transform: 'translate(-50%, -50%)'
        }
        toRender = <div style={divStyle}><PulseLoader
          loading={this.state.loading}
        /></div>
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
