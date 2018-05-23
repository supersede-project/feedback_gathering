import React, {Component} from 'react';
import TiNews from 'react-icons/lib/ti/news';


import style from '../css/App.css';

class PromotingBanner extends Component {

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            showFeedback: false,
            toggled: false
        }
        this.fetchCompanyFeedbacks = this.fetchCompanyFeedbacks.bind(this);
        this.toggleMessage = this.toggleMessage.bind(this);
    }

    componentDidMount() {
        this.fetchCompanyFeedbacks();
    }

    fetchCompanyFeedbacks() {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/' + sessionStorage.getItem('applicationId') + '/feedbacks/feedback_company', {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            }
        }).then(result => result.json())
            .then(result => {
                that.setState({data: result})
            });
    }

    toggleMessage(e){
        this.setState({toggled: !this.state.toggled});
    }

    render() {
        var that = this;
        if(!that.state.toggled) {
            return (
                <div className={style.promotingbanner}
                     style={{
                         position: this.props.positioning,
                         background: ' #e6e6e6'
                     }}>
                    {this.state.data.map(function (item, index) {
                        if (item.promote === true) {
                            return (<div>
                                <h5 align="left" style={{
                                    fontSize: 10,
                                    fontStyle: 'italic',
                                    color: 'black'
                                }} onClick={that.toggleMessage}>
                                    <TiNews size={35}
                                            padding={75}/>&nbsp; {(item.text.length > 30) ? item.text.substring(0, 30) + "..." : item.text}
                                </h5>
                            </div>);
                        }
                    })}
                </div>
            );
        }
        else if(that.state.toggled){
            return (
                <div className={style.promotingbanner}
                     style={{
                         position: this.props.positioning,
                         background: '#e6e6e6'
                     }}>
                    <p onClick={that.toggleMessage} style={{color: 'red', fontStyle: 'oblique', fontSize: 10}}>Check out the forum for more details</p>
                </div>
            );

        }
    }
}

export default PromotingBanner;
