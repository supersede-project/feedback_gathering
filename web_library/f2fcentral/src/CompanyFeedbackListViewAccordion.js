import React, {Component} from 'react';
import TiNews from 'react-icons/lib/ti/news';

import style from './App.css';


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
        this.fetchData(null);
    }

    fetchData(e) {
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

    render()
    {
        let toRender = null;
        var that = this;

        toRender =
            <div>
                <Accordion>
            {that.state.data.map(function (item, index) {
                if(!item.hasOwnProperty("textFeedbacks") && !this.state.expanded)
                {
                    return (
                        <div style={{display: "flex", justifyContent: "space-around", background: 'linear-gradient(to bottom, #e6f9ff 0%, #ccf3ff 50%, #b3edff 52%, #99e7ff 100%)'}}>
                            <h5 align="left" style={{
                                flexGrow: 2,
                                fontSize: 12,
                                fontStyle: 'italic'
                            }}
                                onClick={this.toggleExpanded}><TiNews size={35} padding={75}/>&nbsp; {(!this.state.expanded && item.title.length > 30) ? item.title.substring(0, 30) + "...more" : item.title}
                            </h5>
                        </div>
                    )
                }
                else if(this.state.expanded){
                    return (
                        <div style={{display: "flex", justifyContent: "space-around", background: 'linear-gradient(to bottom, #e6f9ff 0%, #ccf3ff 50%, #b3edff 52%, #99e7ff 100%)'}}>
                            <h5 align="left" style={{
                                flexGrow: 2,
                                fontSize: 12,
                                fontStyle: 'italic'
                            }}
                                onClick={this.toggleExpanded}><TiNews size={35} padding={75}/>&nbsp; {(!this.state.expanded && item.title.length > 30) ? item.title.substring(0, 30) + "...less" : item.title}
                            </h5>
                        </div>
                    )
                }
                return false;
            })}
                </Accordion>
            </div>;

    }

}

export default CompanyFeedbackListViewAccordion;
