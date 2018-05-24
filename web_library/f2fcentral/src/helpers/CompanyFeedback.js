import React, {Component} from 'react';
import TiNews from 'react-icons/lib/ti/news';
import IoFlashOff from 'react-icons/lib/io/flash-off';
import IoFlash from 'react-icons/lib/io/flash';

import style from '../css/App.css';


class CompanyFeedback extends Component {

    constructor(props) {
        super(props);

        this.state = {
            expanded: false
        }
        this.toggleExpanded = this.toggleExpanded.bind(this);
        this.handleUnpromote = this.handleUnpromote.bind(this);
        this.handlePromotingStatus = this.handlePromotingStatus.bind(this);

    }

    handleUnpromote(){
        if(window.adminUser) {
            if(this.props.promote === true){
                return <IoFlash size={35} padding={75} onClick={this.handlePromotingStatus}/>
            }
            else if(this.props.promote ===false){
                return <IoFlashOff size={35} padding={75} onClick={this.handlePromotingStatus}/>
            }
            else if(this.props.promote === null){
                return <IoFlashOff size={35} padding={75} onClick={this.handlePromotingStatus}/>
            }
        }
    }

    handlePromotingStatus(e){
        var promoteValue = "";
        if(this.props.promote === true) {
            promoteValue = false;
        }
        else if(this.props.promote ===false || this.props.promote === null){
            promoteValue = true;
        }
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedback_company/promote/'+ this.props.companyFeedbackId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            },
            body: JSON.stringify({
                promote: promoteValue
            })
        }).then(result => this.props.update());
        e.stopPropagation();

    }

    toggleExpanded(e) {
        if (!this.props.visibility) {
            this.setState({expanded: !this.state.expanded});
        }
    }

    render()
    {
        return (
            <div style={{display: "flex", justifyContent: "space-around", background: '#FFFCDB'}}>
                <h5 align="left" style={{
                    flexGrow: 2,
                    fontSize: 12,
                    fontStyle: 'normal'
                }}
                    onClick={this.toggleExpanded}><TiNews size={35} padding={75}/>&nbsp; {(!this.state.expanded && this.props.title.length > 30) ? this.props.title.substring(0, 30) + "..." : this.props.title}

                </h5>
                <div className={style.iconContainer}>
                    {this.handleUnpromote()}
                </div>
            </div>
        );
    }
}

export default CompanyFeedback;
