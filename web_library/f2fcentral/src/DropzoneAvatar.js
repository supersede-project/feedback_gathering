import React, {Component} from 'react';
import Dropzone from 'react-dropzone';
import style from './App.css';

class DropzoneAvatar extends Component {

    constructor(props) {
        super(props);
        this.state = {
            files: null,
            disabled: true,
            successful: false
        }
        this.backButtonPressed = this.backButtonPressed.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    onDrop(files){
        this.setState({files: files[0]});
    }

    backButtonPressed(e){
        this.props.onDropzoneBackButtonSelected();
    }

    submitForm(e){
        var that = this;
        var formData = new FormData();

        formData.append("file", this.state.files);
            fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/uploadImage/user/' + sessionStorage.getItem('userId'), {
                method: 'POST',
                headers: {
                    //'Content-Type': 'multipart/formdata',
                    'Authorization': sessionStorage.getItem('token')
                },
                body: formData
            }).then(result => {
                that.setState({files: null, successful:true});
            });

    }


    render(){

        var fileLines = '';

        if(this.state.files !== null) {
            fileLines = <li key={this.state.files.name}>{this.state.files.name} - {this.state.files.size} bytes</li>;
        }

        var successful = '';

        if(this.state.successful) {
            successful = <span className={style.successful}>Upload successful</span>;
        }

        return (

            <section className={style.companyfeedbackform}>
                <h2>Upload new avatar</h2>
                <div>
                    {successful}
                </div>
                <div className={style.companyfeedbackform}>
                    <Dropzone className={style.dropzone} accept="image/png" onDrop={this.onDrop.bind(this)}>
                        <p>Try dropping a file here, or click to select file to upload.</p>
                        <p>Only *.png images will be accepted</p>
                    </Dropzone>
                </div>
                <aside>
                    <h2>Dropped file information</h2>
                    <ul>
                        {fileLines}
                    </ul>
                </aside>
                <div>
                <button className={style.formbuttons1} type="button" onClick={this.submitForm}>Upload</button>&nbsp;
                <button className={style.formbuttons1} type="button" onClick={this.backButtonPressed}>Back</button>
                </div>
            </section>
        );

    }

}

export default DropzoneAvatar;