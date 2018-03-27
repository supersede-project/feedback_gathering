import React, {Component} from 'react';
import TiNews from 'react-icons/lib/ti/news';
import IoFlashOff from 'react-icons/lib/io/flash-off';
import IoFlash from 'react-icons/lib/io/flash';

import style from './App.css';


class CompanyFeedback extends Component {

    constructor(props) {
        super(props);

        this.state = {
            expanded: false
        }
        this.toggleExpanded = this.toggleExpanded.bind(this);
        this.handleUnpromote = this.handleUnpromote.bind(this);
    }

    handleUnpromote(){
        if(window.adminUser) {
            if(this.props.promote === true){
                return <IoFlash size={35} padding={75}/>
            }
            else if(this.props.promote ===false){
                return <IoFlashOff size={35} padding={75}/>
            }
        }
    }

    toggleExpanded(e) {
        if (!this.props.visibility) {
            this.setState({expanded: !this.state.expanded});
        }
    }

    render()
    {
        return (
            <div style={{display: "flex", justifyContent: "space-around", background: 'linear-gradient(to bottom, #e6f9ff 0%, #ccf3ff 50%, #b3edff 52%, #99e7ff 100%)'}}>
                <h5 align="left" style={{
                    flexGrow: 2,
                    fontSize: 12,
                    fontStyle: 'italic'
                }}
                    onClick={this.toggleExpanded}><TiNews size={35} padding={75}/>&nbsp; {(!this.state.expanded && this.props.title.length > 30) ? this.props.title.substring(0, 30) + "..." : this.props.title}

                </h5>
                <div className={style.iconContainer}>
                    {this.handleUnpromote()}
                    <span className={style.tooltiptext}>Select to unpromote entry</span>
                </div>
            </div>
        );
    }
}

export default CompanyFeedback;
