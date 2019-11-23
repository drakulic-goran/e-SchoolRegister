import React, { Component } from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSave, faTimes } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/EditUser.css';


class ParentForm extends Component{


render(){
    return(
        <form id="register_form">
            <div id="error_msg"></div>
            <div>
                <label htmlFor='firstName'>First name </label>
                <input
                    type = 'text'
                    name = 'firstName'
                    defaultValue = {this.props.firstName}
                    placeholder = 'Enter first name'
                    id="firstName"
                    onChange = {this.props.onChange}
                    readOnly={this.props.isAdmin? null : "readonly"} ></input>
            </div>
            <div>
                <label htmlFor='lastName'>Last name </label>
                <input
                    type='text'
                    name = 'lastName'
                    defaultValue = {this.props.lastName}
                    placeholder = 'Enter last name'
                    id="lastName"
                    onChange = {this.props.onChange}
                    readOnly={this.props.isAdmin? null : "readonly"} ></input>
            </div>
            <div>
                <label htmlFor='jMBG'>JMBG </label>
                <input
                    type='text'
                    name = 'jMBG'
                    defaultValue = {this.props.jMBG}
                    placeholder = 'Enter JMBG'
                    id="jMBG"
                    onChange = {this.props.onChange}
                    readOnly={this.props.isAdmin? null : "readonly"} ></input>
                <span></span>
            </div>
            <div>
                <label htmlFor='gender'>Gender </label>
                <label id="gender">
                    <input
                        type='radio'
                        name = 'gender'
                        defaultValue = 'GENDER_MALE'
                        id="male"
                        defaultChecked = {this.props.gender === 'GENDER_MALE'}
                        onChange = {this.props.onChange}
                        disabled={this.props.isAdmin? null : "disabled"} />
                    Male 
                </label>
                <label id="gender">
                    <input
                        type='radio'
                        name = 'gender'
                        defaultValue = 'GENDER_FEMALE'
                        id="female"
                        defaultChecked = {this.props.gender === 'GENDER_FEMALE'}
                        onChange = {this.props.onChange}
                        disabled={this.props.isAdmin? null : "disabled"} />
                    Female 
                </label>
            </div>
            <div>
                <label htmlFor='email'>Email </label>
                <input
                    type='email'
                    name = 'email'
                    defaultValue = {this.props.email}
                    placeholder = 'Enter e-mail'
                    id="email"
                    onChange = {this.props.onChange}
                    readOnly={this.props.isAdmin? null : "readonly"} ></input>
                <span></span>
            </div>
            <div>
                <label htmlFor='username'>Username </label>
                <input
                    type='text'
                    name = 'username'
                    defaultValue = {this.props.username}
                    placeholder = 'Enter username'
                    id="username"
                    onChange = {this.props.onChange}></input>
                <span></span>
            </div>
            {this.props.isEditing ?
                <div>
                    <label htmlFor='oldPassword'>Old password </label>
                    <input
                        type='password'
                        name = 'oldPassword'
                        placeholder = 'Enter old password'
                        id="oldPassword"
                        onChange = {this.props.onChange}></input>
                </div>
            : null}
            <div>
                <label htmlFor='password'>New password </label>
                <input
                    type='password'
                    name = 'password'
                    placeholder = 'Enter password'
                    id="password"
                    onChange = {this.props.onChange}></input>
            </div>
            <div>    
                <label htmlFor='confirmedPassword'>Confirm new password </label>
                <input
                    type='password'
                    name = 'confirmedPassword'
                    placeholder = 'Confirm new password'
                    id="confirmedPassword"
                    onChange = {this.props.onChange}></input>
            </div>
            <div>
            <button type="button"
                        name="onCancel"
                        onClick={this.props.onCancel}
                        id="cncl_btn">
                <FontAwesomeIcon icon={faTimes} />
                <span> Cancel</span>
            </button>
            <button type="button"
                        name="onParentSubmit"
                        onClick={this.props.onParentSubmit}
                        id="reg_btn">
                <FontAwesomeIcon icon={faSave} />
                <span> Save</span>
            </button>
            </div>
        </form>
    )
}
}
export default ParentForm