import React, {Component} from 'react';

const items= [
    'No notifications',
    'E-Mail',
    'Public',
    'Private',
];

class FeedbackSettings extends Component {


    constructor(props) {
        super(props);
        this.state = {
            selected: false
        }
        this.openSelected = this.openSelected.bind(this);
    }

    openSelected(){
        this.setState({selected: !this.state.selected});
    }

    render()
    {
        return (
           <div>
               <input type="checkbox"/>
               <label>No notification</label>
           </div>

        );
    }
}

export default FeedbackSettings;
