import React, {Component} from 'react';
import style from './App.css';

class CompanyFeedbackInputForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedOption: '',
            isChecked: false,
            formText: ''
        };

        this.backButtonPressed = this.backButtonPressed.bind(this);
        this.handleSelection = this.handleSelection.bind(this);
        this.handleTextareaTextInput = this.handleTextareaTextInput.bind(this);
        this.changeCheckbox = this.changeCheckbox.bind(this);
    }

    backButtonPressed(e){
        this.props.onBackButtonSelected();
    }

    handleSelection(selectedOption){
        this.setState({selectedOption: selectedOption});
        console.log(`Selected: ${selectedOption.value}`);
    }

    handleTextareaTextInput(e){
        this.setState({formText: e.target.value});
    }

    changeCheckbox(){
        this.setState({isChecked: !this.state.isChecked});
    }


    submitForm(e){
        let postFeedback = {
            status: this.state.selectedOption.value,
            text: this.state.formText,
            promote: this.state.isChecked
        }
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/feedback_company/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': sessionStorage.getItem('token')
            },
            body: JSON.stringify(postFeedback)
        }).then(result=> that.props.update());
        e.stopPropagation();
    }


    render(){
         return (<div className={style.companyfeedbackform}><h2>Create new forum entry</h2>
             <div>
                <form>Please enter following information to add a new entry<br/>
                    <div>
                    Entry title: <input type="text" name="feedbackTitle"/><br/><br/>
                    <div className={style.companyfeedbacktext}>
                    Entry text:
                    <textarea name="feedbacktext" rows="4" cols="50" value={this.state.formText} onChange={this.handleTextareaTextInput} placeholder="Please enter your text here...">
                    </textarea><br/></div>
                    <div>
                        <input type="checkbox" name="promoting" checked={this.state.isChecked} onChange={this.changeCheckbox}/>Promote
                    </div>
                    </div>
                    <div>
                    <select name="feedbackStatus" required={true} value={this.state.selectedOption.value} onChange={this.handleSelection}>
                        <option value="completed">Completed</option>
                        <option value="inProgress">In Progress</option>
                        <option value="declined">Declined</option>
                        <option value="received">Received</option>
                    </select><br/></div>
                    <div>
                    <button type="button" className={style.formbuttons1} onClick={this.submitForm}>Submit</button><br/>
                    <button type="button" className={style.formbuttons1} onClick={this.backButtonPressed}>Back</button>
                    </div>
                </form>
             </div>
            </div>);

    }

}

export default CompanyFeedbackInputForm;