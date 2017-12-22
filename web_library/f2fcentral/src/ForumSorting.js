import React, {Component} from 'react';

import './App.css';
import 'react-tabs/style/react-tabs.css';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import './Dropdown.css';

class ForumSorting extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedOption: '',
            selectedOptionValue: ''
        };

    }

    handleChange(selectedOption) {
        this.setState({selectedOption: selectedOption});
        console.log(`Selected: ${selectedOption.label}`);
    }

    render() {
        return (
            <Select
                name="form-field-name"
                value={this.state.selectedOption.value}
                onChange={this.handleChange}
                options={[
                    {value: 'date1', label: 'Decreasing date (Newest first)'},
                    {value: 'date2', label: 'Increasing date (Oldest first)'},
                    {value: 'myfeedback', label: 'My feedbacks'},
                    {value: 'unseen', label: 'Unread feedbacks'},
                    {value: 'unrated', label: 'Unrated feedbacks'},
                    {value: 'mostlike', label: 'Most liked feedbacks'},
                    {value: 'company', label: 'Company entries'},
                    {value: 'status1', label: 'Status received'},
                    {value: 'status2', label: 'In progress'},
                    {value: 'status3', label: 'Declined'},
                    {value: 'status4', label: 'Completed'},

                ]}
            />

        );
    }

    update() {
        this.props.onUpdate(this.state.selectedOption);
    }
}

export default ForumSorting;