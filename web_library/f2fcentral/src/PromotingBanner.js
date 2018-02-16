import React, {Component} from 'react';
import TiNews from 'react-icons/lib/ti/news';


import style from './App.css';

class PromotingBanner extends Component {

    constructor(props) {
        super(props);
        this.state = {
            data: []
        }
        this.fetchCompanyFeedbacks = this.fetchCompanyFeedbacks.bind(this);
    }

    componentDidMount(){
        this.fetchCompanyFeedbacks();
    }

    fetchCompanyFeedbacks(){
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/feedback_company', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': sessionStorage.getItem('token')
                }
            }).then(result => result.json())
                .then(result => {
                    this.setState({data: result})
                });
        }

    render() {
        return (
                <div>
                    {this.state.data.map(function(item, index) {
                    <div style={{
                        display: "flex",
                        justifyContent: "space-around",
                        background: 'linear-gradient(to top, lightgrey 0%, lightgrey 1%, #e0e0e0 26%, #efefef 48%, #d9d9d9 75%, #bcbcbc 100%)'
                    }}>
                        <h5 align="left" style={{
                            flexGrow: 2,
                            fontSize: 12,
                            fontStyle: 'italic'
                        }}><TiNews size={35}
                                   padding={75}/>&nbsp; {(item.text.length > 30) ? item.text.substring(0, 30) + "..." : item.text}
                        </h5>
                    </div>
                    })}
                </div>
        );
    }
}

export default PromotingBanner;
