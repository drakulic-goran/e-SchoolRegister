import React, { Component } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEdit, faTrash, faUserPlus, faCompactDisc, faPlusCircle, faEyeSlash, faEye } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/Administration.css'
//import AssignUnassignClasses from '../AssignUnassign/AssignUnassignClasses';
import DepartmentForm from '../DepartmentForm/DepartmentForm';


let _ = require('lodash');


class AdministrationDepartments extends Component {
    constructor(props) {
        super(props)
        this.state = {
            loggedUser: this.props.loggedUser,
            departments: [],
            departmentsWithClass: null,
            departmentsWithOutClass: null,
            selectedDepartment: null,
            newDepartment: null,
            savingDepartment: null,
            isLoading: false,
            isError: false,
            errMsg: '',
            isCatchError: false,
            classes: [],
            selectedDepartmentForClasses: null,
            activeDepartmentView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/department/withclass/', {
            mode: 'cors',
            method: 'GET',
            headers: {
                'Authorization': 'Basic ' + window.btoa(this.props.username + ":" + this.props.password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },).catch((error) => {
                alert(error.toString().substr(4) + "!")
                this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
            })
        if( !this.state.isCatchError && response.ok ) {
            const departments = await response.json();
            //console.log(departments)

            this.setState({ departmentsWithClass: departments })

            const response1 = await fetch('http://localhost:8080/project/class/', {
                mode: 'cors',
                method: 'GET',
                headers: {
                    'Authorization': 'Basic ' + window.btoa(this.props.username + ":" + this.props.password),
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8', 
                    'Origin': 'http://localhost:3000', }
                },).catch((error) => {
                    alert(error.toString().substr(4) + "!")
                    this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
                })
            if( !this.state.isCatchError && response1.ok ) {
                const classes = await response1.json();
                this.setState({ classes: classes })
                //console.log(this.state.classes)
            } else if (!this.state.isCatchError) {
                this.setState({ isLoading: false, isError: true })
                const err = await response1.json()
                if(err.errors) {
                    let error = ""
                    for (let i = 0; i<err.errors.length; i++) {
                        error += (err.errors[i].defaultMessage + " ")
                    }
                    alert("Validation error/s - " + error)
                } else {
                    alert("Error: " + err.message)
                }
            }

            const response2 = await fetch('http://localhost:8080/project/department/', {
                mode: 'cors',
                method: 'GET',
                headers: {
                    'Authorization': 'Basic ' + window.btoa(this.props.username + ":" + this.props.password),
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8', 
                    'Origin': 'http://localhost:3000', }
                },).catch((error) => {
                    alert(error.toString().substr(4) + "!")
                    this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
                })
            if( !this.state.isCatchError && response2.ok ) {
                const departments1 = await response2.json();
                //console.log(departments1)
                this.setState({ departmentsWithOutClass: departments1 })
            } else if (!this.state.isCatchError) {
                this.setState({ isLoading: false, isError: true })
                const err = await response2.json()
                if(err.errors) {
                    let error = ""
                    for (let i = 0; i<err.errors.length; i++) {
                        error += (err.errors[i].defaultMessage + " ")
                    }
                    alert("Validation error/s - " + error)
                } else {
                    alert("Error: " + err.message)
                }
            }    

        } else if (!this.state.isCatchError) {
            this.setState({ isLoading: false, isError: true })
            const err = await response.json()
            if(err.errors) {
                let error = ""
                for (let i = 0; i<err.errors.length; i++) {
                    error += (err.errors[i].defaultMessage + " ")
                }
                alert("Validation error/s - " + error)
            } else {
                alert("Error: " + err.message)
            }
        }
        let departments2 = []
        this.state.departmentsWithOutClass.forEach((dep) => departments2 = [...departments2, {department: dep, clas: {classLabel: null}, departmentClass: {schoolYear: null} }] )
        //console.log(departments2)
        const result = _.unionBy(this.state.departmentsWithClass, departments2, 'department.id');
        //console.log(result)
        this.setState({ departments: result, isLoading: false })
        //console.log(this.state.departments)

    }

    shouldComponentUpdate(nextProps, nextState) {
        if (this.state !== nextState) {
            return true;
        }
        return false;
      }

    handleDepartmentsClick = (id) => {
        this.state.activeDepartmentView !== id ?
            this.setState({activeDepartmentView: id})
        : this.setState({activeDepartmentView: null})
    }

    renderSubjectData() {
        return this.state.departments.map((department, id) => (
            <div className="administration-header" key={department.department.id}><h2>{department.clas.classLabel ? department.clas.classLabel+"-" : null}{department.department.departmentLabel} {department.departmentClass.schoolYear ? "(School year: "+department.departmentClass.schoolYear+")" : null}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeDepartmentView !== id ? faEye : faEyeSlash} onClick={() => this.handleDepartmentsClick(id)} /></h2>
                <div className={this.state.activeDepartmentView === id ? "" : "hide"}>   
                    <span className="user-info"><h4>Enrollment year</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {department.department.enrollmentYear}
                    </span>
                    <div>
                        <button type="button"
                                    name="onDelete"
                                    onClick={ () => this.handleDelete(department.department.id) }
                                    id="edit_delete_btn">
                            <FontAwesomeIcon icon={faTrash} /> 
                            <span> DELETE</span>
                        </button>
                        <button type="button"
                                    name="onEdit"
                                    onClick={ () => this.handleEdit(department.department.id) }
                                    id="edit_delete_btn">
                            <FontAwesomeIcon icon={faEdit} />
                            <span> EDIT</span>
                        </button>
                    </div>
                </div>
            </div>
        ))
    }

    handleDelete = async (departmentId) => {
        //console.log(departmentId)
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/department/'+departmentId, {
            mode: 'cors',
            method: 'DELETE',
            headers: {
                'Authorization': 'Basic ' + window.btoa(this.props.username + ":" + this.props.password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },).catch((error) => {
                alert(error.toString().substr(4) + "!")
                this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
            })
        if( !this.state.isCatchError && response.ok ) {
            //console.log(departmentId)
            const departments = this.state.departments.filter((subject) => subject.department.id !== departmentId)
            this.setState({ departments: departments, isLoading: false, activeDepartmentView: null })
            //console.log(departments)
            alert("Department deleted.")
        } else if(!this.state.isCatchError) {
            this.setState({ isLoading: false, isError: true })
            const err = await response.json()
            if(err.errors) {
                let error = ""
                for (let i = 0; i<err.errors.length; i++) {
                    error += (err.errors[i].defaultMessage + " ")
                }
                alert("Validation error/s - " + error)
            } else {
                alert("Error: " + err.message)
            }
        }
    }

    handleEdit = (departmentId) => { const department = this.state.departments.find( de => de.department.id === departmentId ); this.setState({ selectedDepartment: {department: department.department, clas: department.clas ? department.clas : null, departmentClass: department.departmentClass ? department.departmentClass : null, departmentLabel: department.department.departmentLabel, enrollmentYear: department.department.enrollmentYear, department_class: department.clas ? department.clas.classLabel : null, schoolYear: department.departmentClass ? department.departmentClass.schoolYear : null} }); }

    handleEditSubmit = async (e) => {
        e.preventDefault()
        //console.log(this.state.selectedDepartment)
        //console.log(this.state.savingDepartment)
        let chckSavingDepartment = null
        if(this.state.savingDepartment.departmentLabel && this.state.savingDepartment.departmentLabel !== "" && this.state.savingDepartment.departmentLabel !== " ")
            chckSavingDepartment = { ...chckSavingDepartment, departmentLabel: this.state.savingDepartment.departmentLabel}
        if(this.state.savingDepartment.enrollmentYear && this.state.savingDepartment.enrollmentYear !== "" && this.state.savingDepartment.enrollmentYear !== " ")
            chckSavingDepartment = { ...chckSavingDepartment, enrollmentYear: this.state.savingDepartment.enrollmentYear}
        if(this.state.savingDepartment.department_class && this.state.savingDepartment.department_class !== "" && this.state.savingDepartment.department_class !== " ")
            chckSavingDepartment = { ...chckSavingDepartment, department_class: this.state.savingDepartment.department_class}
        if(this.state.savingDepartment.schoolYear && !this.state.savingDepartment.schoolYear !== "" && this.state.savingDepartment.schoolYear !== " ")
            chckSavingDepartment = { ...chckSavingDepartment, schoolYear: this.state.savingDepartment.schoolYear}
        //console.log(chckSavingDepartment)
        this.setState({ savingDepartment: chckSavingDepartment })
        if (chckSavingDepartment) {
            this.setState({ isLoading: true })
            //console.log(this.state.selectedDepartment)
            //console.log(this.state.savingDepartment)
            const response = await fetch('http://localhost:8080/project/department/'+this.state.selectedDepartment.department.id, {
                mode: 'cors',
                method: 'PUT',
                body: JSON.stringify(chckSavingDepartment),
                headers: {
                    'Authorization': 'Basic ' + window.btoa(this.props.username + ":" + this.props.password),
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8', 
                    'Origin': 'http://localhost:3000', }
                },).catch((error) => {
                    alert(error.toString().substr(4) + "!")
                    this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
                })
            if( !this.state.isCatchError && response.ok ) {
                const departments = [...this.state.departments]
                const index = departments.findIndex((department) => department.department.id === this.state.selectedDepartment.department.id)
                let formatedDepartment = {...this.state.selectedDepartment}
                if(chckSavingDepartment.departmentLabel)
                    formatedDepartment.department.departmentLabel = chckSavingDepartment.departmentLabel
                if(chckSavingDepartment.enrollmentYear)
                    formatedDepartment.department.enrollmentYear = chckSavingDepartment.enrollmentYear
                if(chckSavingDepartment.department_class)
                    formatedDepartment.clas.classLabel = chckSavingDepartment.department_class
                if(chckSavingDepartment.schoolYear)
                    formatedDepartment.departmentClass.schoolYear = chckSavingDepartment.schoolYear
                //console.log(formatedDepartment)
                departments.splice(index, 1, formatedDepartment)
                this.setState({ departments, selectedDepartment: null, savingDepartment: null, isLoading: false })
                alert("Department changed.")
            } else {
                this.setState({ isLoading: false, isError: true })
                const err = await response.json()
                if(err.errors) {
                    let error = ""
                    for (let i = 0; i<err.errors.length; i++) {
                        error += (err.errors[i].defaultMessage + " ")
                    }
                    alert("Validation error/s - " + error)
                } else {
                    alert("Error: " + err.message)
                }
            }
        }
    }

    handleNew = () => { this.setState({newDepartment: { } }); }

    handleNewSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        //console.log(this.state.newDepartment)
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/department/', {
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(this.state.newDepartment),
            headers: {
                'Authorization': 'Basic ' + window.btoa(username + ":" + password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },).catch((error) => {
                alert(error.toString().substr(4) + "!")
                this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
            })
        if( !this.state.isCatchError && response.ok ) {
            const department = await response.json();
            const departments = [...this.state.departments]
            departments.push({department: department, clas: {classLabel: this.state.newDepartment.department_class}, departmentClass: {schoolYear: this.state.newDepartment.schoolYear} })
            //console.log(departments)
            this.setState({ departments: departments, newDepartment: null, isLoading: false })
            alert("Department added.")
            /*const response = await fetch('http://localhost:8080/project/department/withclass/', {
                mode: 'cors',
                method: 'GET',
                headers: {
                    'Authorization': 'Basic ' + window.btoa(this.props.username + ":" + this.props.password),
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8', 
                    'Origin': 'http://localhost:3000', }
                },).catch((error) => {
                    alert(error.toString().substr(4) + "!")
                    this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
                })
            if( !this.state.isCatchError && response.ok ) {
                const departments = await response.json();
                //console.log(subjects)
                this.setState({ departments: departments, newDepartment: null, isLoading: false })
                //console.log(this.state.subjects)
                alert("Department added.")
            } else if (!this.state.isCatchError) {
                this.setState({ isLoading: false, isError: true })
                const err = await response.json()
                if(err.errors) {
                    let error = ""
                    for (let i = 0; i<err.errors.length; i++) {
                        error += (err.errors[i].defaultMessage + " ")
                    }
                    alert("Validation error/s - " + error)
                } else {
                    alert("Error: " + err.message)
                }
            }*/
        } else if (!this.state.isCatchError) {
            this.setState({ newDepartment: null, isLoading: false, isError: true })
            const err = await response.json();
            if(err.errors) {
                let error = ""
                for (let i = 0; i<err.errors.length; i++) {
                    error += (err.errors[i].defaultMessage + " ")
                }
                alert("Validation error/s - " + error)
            } else {
                alert("Error: " + err.message)
            }
        }
    }

    handleChange = (e) => {
        e.preventDefault()
        this.state.selectedDepartment ? 
            this.setState({
                selectedDepartment: {
                ...this.state.selectedDepartment,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                },
                savingDepartment: {
                ...this.state.savingDepartment,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                }
        }) : 
            this.setState({
                newDepartment: {
                ...this.state.newDepartment,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                }
        })
    }

    handleCancel = () => { this.setState({ selectedDepartment: null, newDepartment: null, savingDepartment: null, selectedDepartmentForClasses: null }); }


    render() {

        const {departments, selectedDepartment, newDepartment, classes} = this.state

        return departments.length > 0 || newDepartment 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    { !newDepartment && !selectedDepartment &&
                        <div>
                            <button type="button"
                                        name="onAddNew"
                                        onClick={this.handleNew}
                                        id="add_btn">
                                <FontAwesomeIcon icon={faPlusCircle} />
                                <span> Add new</span>
                            </button>

                            <h1 id='title'>LIST OF DEPARTMENTS</h1>

                            <div>
                                {this.renderSubjectData()}
                            </div>
                        </div>
                    }

                    { newDepartment && 
                        <div>
                            <DepartmentForm 
                                {...newDepartment}
                                classes={classes}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleNewSubmit} /> 
                        </div>
                    }

                    { selectedDepartment && 
                        <div>
                            <DepartmentForm 
                                {...selectedDepartment}
                                classes={classes}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleEditSubmit} /> 
                        </div>
                    }

                </React.Suspense>
                    
            </div>
        )
        : <div>
            <button type="button"
                        name="onAddNew"
                        onClick={this.handleNew}
                        id="reg_btn">
                <FontAwesomeIcon icon={faUserPlus} />
                <span> Add new</span>
            </button>
        </div>
    }
}

export default AdministrationDepartments