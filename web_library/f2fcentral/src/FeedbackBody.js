import React, {Component} from 'react';

import FaBug from 'react-icons/lib/fa/bug';
import FaThumbsOUp from 'react-icons/lib/fa/thumbs-o-up';
import FaThumbsODown from 'react-icons/lib/fa/thumbs-o-down';
import FaWechat from 'react-icons/lib/fa/wechat';
import FaLightbulbO from 'react-icons/lib/fa/lightbulb-o';
import FaHandPeaceO from 'react-icons/lib/fa/hand-peace-o';

class FeedbackBody extends Component {

    render()
    {
        return (<div><div align="left" style={{fontSize: 10}}>sent on {this.props.date}</div>
            <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Status: {this.props.status}</div>
            <div align="left" style={{fontSize: 10, color: '#169BDD'}}>Forum activity: <FaThumbsOUp size={20}/><FaThumbsODown size={20} color={'black'} padding={10}/> <FaWechat size={20}color={'#63C050'} padding={10} /></div></div>);
    }
}

export default FeedbackBody;
