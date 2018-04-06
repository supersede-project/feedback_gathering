import React, {Component} from 'react';
import TiNews from 'react-icons/lib/ti/news';

import style from '../css/App.css';
import { Accordion, AccordionItem } from 'react-sanfona';
import 'react-tabs/style/react-tabs.css';
import { PulseLoader } from 'react-spinners';
import CompanyFeedback from "../helpers/CompanyFeedback";

class CompanyFeedbackListViewAccordion extends Component {

    constructor(props) {
        super(props);

        this.state = {
            data: []
        }
        this.fetchData = this.fetchData.bind(this);
        this.toggleExpanded = this.toggleExpanded.bind(this);
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        console.log("FetchData called");
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedback_company', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result=>result.json())
            .then(result=> {

                that.setState({data: result})
            });
    }

    toggleExpanded() {
        this.setState({expanded: !this.state.expanded});
    }

    render() {
        let that = this;

        return <Accordion>
            {that.state.data.map(function (item, index) {
                if(item.text.length >0){
                 return(
                     <AccordionItem titleTag="span" title={<CompanyFeedback title={item.text} promote={item.promote} companyFeedbackId={item.id} update={that.fetchData}/>}>
                     </AccordionItem>
                 )
                }

            })}
        </Accordion>
    }

}

export default CompanyFeedbackListViewAccordion;
