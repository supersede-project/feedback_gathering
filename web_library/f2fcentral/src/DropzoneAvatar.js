import React, {Component} from 'react';
import Dropzone from 'react-dropzone';
import style from './App.css';

class DropzoneAvatar extends Component {

    constructor(props) {
        super(props);
        this.state = {
            files: [],
            formData: '',
            disabled: true
        }
        this.backButtonPressed = this.backButtonPressed.bind(this);
        this.submitForm = this.submitForm.bind(this);
    }

    onDrop(files){
        this.setState({files});
    }

    backButtonPressed(e){
        this.props.onDropzoneBackButtonSelected();
    }

    submitForm(e){
        var that = this;
        var formData = new FormData();

        var name = '';

        name = this.state.files.map(f => f.name);

            formData.append('name', name);
            formData.append('file', {
                uri: this.state.files.map(f => f.uri),
                type: 'image/png'
            });

        fetch(process.env.REACT_APP_BASE_URL + 'en/applications/'+ sessionStorage.getItem('applicationId')+'/feedbacks/uploadImage/user/' + sessionStorage.getItem('userId'), {
            method: 'POST',
            headers: {
                'Content-Type': 'image/png',
                'Authorization': sessionStorage.getItem('token')
            },
            body: formData
        });
    }

    render(){
        var fname = '';
        return (

            <section>
                <div className={style.companyfeedbackform}>
                    <Dropzone accept="image/png" onDrop={this.onDrop.bind(this)}>
                        <p>Try dropping a file here, or click to select file to upload.</p>
                        <p>Only *.png images will be accepted</p>
                    </Dropzone>
                </div>
                <aside>
                    <h2>Dropped files</h2>
                    <ul>
                        {
                           this.state.files.map(f => <li key={f.name}>{f.name} - {f.size} bytes</li>)
                        }
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