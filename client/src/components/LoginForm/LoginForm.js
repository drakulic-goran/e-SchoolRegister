import React, { Component } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSignInAlt } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/LoginForm.css';
import logo from '../../images/Logo.png';

export class LoginForm extends Component {

/*handleLogin = () => {
    this.props.onLogin()
    this.props.history.push('/students')
}*/


/*handleEmail = (event)=>{
    let emailregex = /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i;
    let emailAdd = event.target.value;
    this.setState({
        Lemail: emailAdd,
    });
    if(emailAdd.match(emailregex)){
      this.setState({
          email: emailAdd,
          LoginMessage: null
      }, ()=> {
        if(this.state.LoginMessage === null || this.state.LoginMessage === undefined || this.state.LoginMessage === "") {
          this.setState({
            hidden: true
          });
        }
      });
    }else{
      this.setState({
        email: null,
        LoginMessage: "Please enter an email address"
      }, ()=>{
        if(this.state.LoginMessage !== null && this.state.LoginMessage !== undefined && this.state.LoginMessage !== "") {
          this.setState({
            hidden: false
          });
        }
      })
    }
  }
  handlePass = (event)=>{
    let pass = event.target.value;
    let passLen = pass.length;
    if(passLen >= 8){
      this.setState({
          Lpass: event.target.value,
          LoginMessage: null
      }, ()=> {
        if(this.state.LoginMessage === null || this.state.LoginMessage === undefined || this.state.LoginMessage === "") {
          this.setState({
            hidden: true
          });
        }
      });
    }else{
      this.setState({
        Lpass: null,
        LoginMessage: "Your password should be 8 or more characters"
      }, ()=>{
        if(this.state.LoginMessage !== null && this.state.LoginMessage !== undefined && this.state.LoginMessage !== "") {
          this.setState({
            hidden: false
          });
        }
      });
    }
  }*/

    render() {
        return (
            <div>
                <img src={logo} alt="Logo" className="login-img" />
                <div className="login-box-container">
                    <div className="login-inner-container">
                        <div className="login-header-logo">
                            Login
                        </div>
                        <div className="login-box">
                            <div className="login-input-group">
                                <label htmlFor='username'>Username</label>
                                <input
                                    id='username' 
                                    type='text'
                                    name='username'
                                    className='login-input'
                                    value={this.props.username}
                                    placeholder='Enter username'
                                    onChange={this.props.onChange} />
                            </div>

                            <div className="login-input-group">
                                <label htmlFor='password'>Password</label>
                                <input 
                                    id='password'
                                    type='password'
                                    name='password'
                                    className='login-input'
                                    value={this.props.password}
                                    placeholder='Enter password'
                                    onChange={this.props.onChange} />
                            </div>

                        </div>

                        <button type="button"
                                    className="login-btn"
                                    onClick={this.props.onLogin}>
                            <FontAwesomeIcon icon={faSignInAlt} />
                            <span> Login</span>
                        </button>

                    </div>
                </div>
            </div>
        )
    }
}

export default LoginForm
