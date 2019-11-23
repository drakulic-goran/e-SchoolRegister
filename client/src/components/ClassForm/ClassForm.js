import React, { Component } from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSave, faTimes } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/EditUser.css';


class ClassForm extends Component{


render(){
    return(
    <form id="register_form">
        <div id="error_msg"></div>
        <div>
            <label htmlFor='subjectName'> Class label</label>
            <input
                type="text" 
                name = 'classLabel'
                defaultValue = {this.props.classLabel}
                placeholder = 'Class label'
                id="classLabel"
                onChange = {this.props.onChange}></input>
        </div>
        <div>
            <button type="button"
                        name="onCancel"
                        onClick={this.props.onCancel}
                        id="reg_btn">
                <FontAwesomeIcon icon={faTimes} />
                <span> Cancel</span>
            </button>
            <button type="button"
                        name="onSubmit"
                        onClick={this.props.onSubmit}
                        id="reg_btn">
                <FontAwesomeIcon icon={faSave} />
                <span> Save</span>
            </button>
        </div>
    </form>
    )
}
}
export default ClassForm