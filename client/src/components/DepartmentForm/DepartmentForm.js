import React, { Component } from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSave, faTimes } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/EditUser.css';


class StudentDepartmentForm extends Component{


render(){
    return(
    <form id="department_form">
        <div id="error_msg"></div>
        <div>
            <label htmlFor='departmentLabel'> Department label</label>
            <input
                type="text" 
                name = 'departmentLabel'
                defaultValue = {this.props.departmentLabel}
                placeholder = 'Department label'
                id="departmentLabel"
                onChange = {this.props.onChange}></input>
        </div>
        <div>
            <label htmlFor='enrollmentYear'> Enrollment year</label>
            <input
                type="text" 
                name = 'enrollmentYear'
                defaultValue = {this.props.enrollmentYear}
                placeholder = 'Enrollment year'
                id="enrollmentYear"
                onChange = {this.props.onChange}></input>
        </div>
        <div>
            <label htmlFor='department_class'> Department class</label>
            <select 
                name = 'department_class'
                defaultValue={this.props.department_class}
                id="department_class"
                onChange={this.props.onChange}>
                <option value=''>--Select--</option>
                {this.props.classes.map(clas => { return <option key={clas.id} value={clas.classLabel}>{clas.classLabel}</option> })}
            </select>
        </div>
        <div>
            <label htmlFor='schoolYear'> School year</label>
            <input
                type="text" 
                name = 'schoolYear'
                defaultValue = {this.props.schoolYear}
                placeholder = 'School year'
                id="schoolYear"
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
export default StudentDepartmentForm