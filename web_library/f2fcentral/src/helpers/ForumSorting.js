import React, {Component} from 'react';

import '../css/App.css';
import 'react-tabs/style/react-tabs.css';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import '../css/Dropdown.css';

class ForumSorting extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedOption: ''
        };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(selectedOption) {
        if(selectedOption === null) {
            selectedOption = {value: ''};
        }
        this.setState({selectedOption: selectedOption});
        this.props.onUpdate(selectedOption.value);
        console.log(`Selected: ${selectedOption.value}`);
    }

    render() {
        return (
            <Select
                name="form-field-name"
                value={this.state.selectedOption.value}
                onChange={this.handleChange}
                placeholder="Please select an option"
                options={[
                    {value: 'date1', label: 'Decreasing date (Newest first)'},
                    {value: 'date2', label: 'Increasing date (Oldest first)'},
                    {value: 'myfeedback', label: 'My feedbacks'},
                    //{value: 'unread', label: 'Unread feedbacks'},
                    {value: 'unrated', label: 'Unrated feedbacks'},
                    {value: 'mostlike', label: 'Most liked feedbacks'},
                    {value: 'company', label: 'Company entries'},
                    {value: 'received', label: 'Received'},
                    {value: 'progress', label: 'In progress'},
                    {value: 'declined', label: 'Declined'},
                    {value: 'completed', label: 'Completed'},

                ]}
            />

        );
    }
}

export default ForumSorting;
