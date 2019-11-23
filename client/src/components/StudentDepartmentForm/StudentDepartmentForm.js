import React, { Component } from "react";
//import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
//import { faSave } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/EditUser.css';


class StudentDepartmentForm extends Component{


render(){
    return(
    <form id="department_form">
        <div id="error_msg"></div>
        <div>
            <label htmlFor='department_class'> Department</label>
            <select 
                name = 'department_class'
                defaultValue={this.props.department_class}
                id="department_class"
                onChange={this.props.onChange}>
                <option value=''>--Select--</option>
                {this.props.departments.map(department => { return <option key={department.department.id} value={department.department.id}>{department.clas.classLabel}-{department.department.departmentLabel}</option> })}
            </select>
        </div>
        <div>
            <label htmlFor='transferDate'> Transfer date</label>
            <input
                type="date" 
                name = 'transferDate'
                defaultValue = {this.props.transferDate}
                placeholder = 'Transfer date'
                id="transferDate"
                onChange = {this.props.onChange}></input>
        </div>
    </form>
    )
}
}
export default StudentDepartmentForm