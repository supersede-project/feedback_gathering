import React, {Component} from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';

class LoginAdmin extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            errorMessage: null
        }
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(event) {
        var that = this;
        fetch(process.env.REACT_APP_BASE_URL + 'authenticate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: this.state.username,
                password: this.state.password
            })
        })
            .then(result => {
                if(result.ok)
                    return result.json()
                return Promise.reject(result.body);
            })
            .then(result => {
                that.props.loginSuccess(result.token);
            }).catch((error) => {
            console.log(error);
            that.setState({errorMessage: error});
        });
        event.stopPropagation();
    }

    render() {
        var errorTag;
        if(this.state.errorMessage !== null) {
            errorTag = <span style={{color: "red"}}>{this.state.errorMessage}</span>;
        }
        return (
            <div>
                <MuiThemeProvider>
                    <div>
                        {errorTag}
                        <AppBar title="Login"/>
                        <TextField hintText="Please enter your API_User name" floatingLabelText="Username"
                                   onChange={(event, newValue) => this.setState({username: newValue})}/>
                        <br/>
                        <TextField type="password" hintText="Please enter your Password" floatingLabelText="Password"
                                   onChange={(event, newValue) => this.setState({password: newValue})}/>
                        <br/>
                        <RaisedButton label="Submit" primary={true} style={style}
                                      onClick={(event) => this.handleClick(event)}/>
                    </div>
                </MuiThemeProvider>
            </div>
        );
    }
}

const style = {
    margin: 15,
};

export default LoginAdmin;