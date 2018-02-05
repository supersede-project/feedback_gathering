import React, {Component} from 'react';
import './App.css';

class CompanyFeedbackInputForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            showModal: false
        }

    }


    render(){
        return (<div><h5>Please enter new thread information</h5>
            <form>New Thread Text:<br/>
                <input type="text" name="feedbacktext"/><br/>
                <select name="feedbackStatus">
                    <option value="completed">Completed</option>
                    <option value="inProgress">In Progress</option>
                    <option value="declined">Declined</option>
                    <option value="received">Received</option>
                </select><br/>
                <input type="submit"/>
            </form></div>);
    }

}

export default CompanyFeedbackInputForm;