import React, { Component } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEdit, faTrash, faUserPlus, faCompactDisc, faEyeSlash, faEye } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/Administration.css'
//import AssignUnassign from '../AssignUnassign/AssignUnassign';
import StudentForm from '../StudentForm/StudentForm'
import StudentDepartmentForm from '../StudentDepartmentForm/StudentDepartmentForm';

let _ = require('lodash');


class AdministrationStudents extends Component {

    constructor(props) {
        super(props)
        this.state = {
            loggedUser: this.props.loggedUser,
            users: [],
            selectedUser: null,
            newUser: null,
            savingUser: null,
            isLoading: false,
            isError: false,
            errMsg: '',
            isCatchError: false,
            selectedParent: null,
            departments: [],
            activeStudentView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })

        let formatedUsersWithDepartment = []
        let formatedUsersWithOutDepartment = []

        const response = await fetch('http://localhost:8080/project/student/withaccount/withdepartment/', {
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
            let allUsers = await response.json();
            //console.log(allUsers)
            allUsers.forEach((user)=>{ user.user.enrollmentDate = user.user.enrollmentDate.split("-").reverse().join("-"); })
            const users = allUsers.filter((user) => user.user.id !== this.state.loggedUser.id)
            
            for (let i = 0; i<users.length; i++) {
                formatedUsersWithDepartment.push({...users[i].user, clas: users[i].clas, department: users[i].department, departmentClass: users[i].departmentClass, userRole: users[i].account.accessRole, username: users[i].account.username})
            }
            //console.log(formatedUsersWithDepartment)

            const response1 = await fetch('http://localhost:8080/project/department/withclass/', {
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
                const departments = await response1.json();
                //console.log(departments)
    
                this.setState({ departments: departments })
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

            const response2 = await fetch('http://localhost:8080/project/student/withaccount/', {
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
                let allUsers1 = await response2.json();
                //console.log(allUsers1)
                allUsers1.forEach((user)=>{ user.user.enrollmentDate = user.user.enrollmentDate.split("-").reverse().join("-"); })
                const users = allUsers1.filter((user) => user.user.id !== this.state.loggedUser.id)
                
                for (let i = 0; i<users.length; i++) {
                    formatedUsersWithOutDepartment.push({...users[i].user, userRole: users[i].account.accessRole, username: users[i].account.username})
                }
                //console.log(formatedUsersWithOutDepartment)
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
        let formatedUsers = []
        formatedUsersWithOutDepartment.forEach((us) => formatedUsers = [...formatedUsers, {...us, clas: {classLabel: null}, departmentClass: {schoolYear: null}, department: {departmentLabel: null} }] )
        //console.log(formatedUsers)
        const result = _.unionBy(formatedUsersWithDepartment, formatedUsers, 'id');
        //console.log(result)
        this.setState({ users: result, isLoading: false })
        //console.log(this.state.users)
    }

    shouldComponentUpdate(nextProps, nextState) {
        if (this.state !== nextState) {
            return true;
        }
        return false;
      }

    handleStudentsClick = (id) => {
        this.state.activeStudentView !== id ?
            this.setState({activeStudentView: id})
        : this.setState({activeStudentView: null})
    }

    renderSubjectData() {
        return this.state.users.map((user, id) => (
            <div className="administration-header" key={id}><h2>{user.firstName} {user.lastName}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeStudentView !== id ? faEye : faEyeSlash} onClick={() => this.handleStudentsClick(id)} /></h2>
                <div className={this.state.activeStudentView === id ? "" : "hide"}>
                {user.clas.classLabel && user.department.departmentLabel && user.departmentClass.schoolYear ? <h2>{user.clas.classLabel+"-"+user.department.departmentLabel+"  (School year: "+user.departmentClass.schoolYear+")"}</h2> : <h2> </h2>}
                    <span className="user-info"><h4>JMBG</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.jMBG}
                    </span>
                    <span className="user-info"><h4>School identification number</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.schoolIdentificationNumber}
                    </span>
                    <span className="user-info"><h4>Enrollment date</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.enrollmentDate}
                    </span>
                    <span className="user-info"><h4>Gender</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.gender === "GENDER_MALE" ? "Male" : "Female"}
                    </span>
                    <span className="user-info"><h4>Username</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.username}
                    </span>
                    <div>
                        <button type="button"
                                    name="onDelete"
                                    onClick={ () => this.handleDelete(user.id) }
                                    id="edit_delete_btn">
                            <FontAwesomeIcon icon={faTrash} /> 
                            <span> DELETE</span>
                        </button>
                        <button type="button"
                                    name="onEdit"
                                    onClick={ () => this.handleEdit(user.id) }
                                    id="edit_delete_btn">
                            <FontAwesomeIcon icon={faEdit} />
                            <span> EDIT</span>
                        </button>
                    </div>
                </div>
            </div>
        ))
    }

    handleDelete = async (userId) => {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/student/'+userId, {
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
            const users = this.state.users.filter((user) => user.id !== userId)
            this.setState({ users, isLoading: false, activeStudentView: null })
            alert("Student deleted.")
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

    handleEdit = (userId) => { const user = this.state.users.find( us => us.id === userId ); this.setState({ selectedUser: user }); }

    handleEditSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        //console.log(this.state.savingUser)
        const response = await fetch('http://localhost:8080/project/student/'+this.state.selectedUser.id, {
            mode: 'cors',
            method: 'PUT',
            body: JSON.stringify(this.state.savingUser),
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

            if(this.state.savingUser.department_class && this.state.savingUser.transferDate) {
                const response1 = await fetch('http://localhost:8080/project/student/'+this.state.selectedUser.id+'/department/'+this.state.savingUser.department_class+'/transferdate/'+this.state.savingUser.transferDate, {
                    mode: 'cors',
                    method: 'PUT',
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
                    //const studentDepratment = await response1.json();
                    //console.log(studentDepratment)
                    const department = this.state.departments.filter((department) => `${department.department.id}` === this.state.savingUser.department_class)
                    //console.log(department)
                    this.setState({ selectedUser: {...this.state.selectedUser, clas: department[0].clas, departmentClass: department[0].departmentClass, department: department[0].department} })
                } else {
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
            } else if (this.state.savingUser.department_class || this.state.savingUser.transferDate) {
                alert("You must enter department and transfer date together, not just one of them.")
            }

            const users = [...this.state.users]
            const index = users.findIndex((user) => user.id === this.state.selectedUser.id)
            users.splice(index, 1, this.state.selectedUser)
            this.setState({ users, selectedUser: null, savingUser: null, isLoading: false })
            alert("Student changed.")

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

    handleNew = () => { this.setState({newUser: { accessRole: "ROLE_STUDENT" } }); }

    handleNewSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        //console.log(this.state.newUser)
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/student/', {
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(this.state.newUser),
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
            let user = await response.json();
            //console.log(user)
            let users = [...this.state.users]
            //console.log(users)
            user.user.enrollmentDate = user.user.enrollmentDate.split("-").reverse().join("-");
            //console.log(department)
            user = {...user.user, userRole: user.accessRole, username: user.username, clas: {classLabel: null}, departmentClass: {schoolYear: null}, department: {departmentLabel: null} }
            //console.log(user)
            //console.log(users)

            if(this.state.newUser.department_class && this.state.newUser.transferDate) {
                const response1 = await fetch('http://localhost:8080/project/student/'+user.id+'/department/'+this.state.newUser.department_class+'/transferdate/'+this.state.newUser.transferDate, {
                    mode: 'cors',
                    method: 'PUT',
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
                    //const studentDepratment = await response1.json();
                    //console.log(studentDepratment)
                    const department = this.state.departments.filter((department) => `${department.department.id}` === this.state.newUser.department_class)
                    user = {...user, clas: department[0].clas, departmentClass: department[0].departmentClass, department: department[0].department }
                    //console.log(user)
                } else {
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
            } else if (this.state.newUser.department_class || this.state.newUser.transferDate) {
                alert("You must enter department and transfer date together, not just one of them.")
            }

            users.push(user)
            this.setState({ users: users, newUser: null, isLoading: false })
            alert("Student added.")
        } else if (!this.state.isCatchError) {
            this.setState({ newUser: null, isLoading: false, isError: true })
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
        this.state.selectedUser ? 
            this.setState({
                selectedUser: {
                ...this.state.selectedUser,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                },
                savingUser: {
                ...this.state.savingUser,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                }
        }) : 
            this.setState({
                newUser: {
                ...this.state.newUser,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                }
        })
    }

    handleCancel = () => { this.setState({ selectedUser: null, newUser: null, selectedParent: null }); }


    render() {

        const {users, selectedUser, newUser, selectedParent, departments} = this.state

        return users.length > 0 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    { !newUser && !selectedUser && !selectedParent &&
                        <div>
                            <button type="button"
                                        name="onAddNew"
                                        onClick={this.handleNew}
                                        id="add_btn">
                                <FontAwesomeIcon icon={faUserPlus} />
                                <span> Add new</span>
                            </button>

                            <h1 id='title'>LIST OF STUDENTS</h1>

                            <div>
                                {this.renderSubjectData()}
                            </div>
                        </div>
                    }

                    { newUser && 
                        <div>
                            <StudentDepartmentForm 
                                departments={departments}
                                onChange={this.handleChange} />
                            <StudentForm 
                                {...newUser}
                                isEditing={false}
                                isAdmin={true}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onStudentSubmit = {this.handleNewSubmit} /> 
                        </div>
                    }

                    { selectedUser && 
                        <div>
                            <StudentDepartmentForm 
                                {...selectedUser.department}
                                departments={departments}
                                onChange={this.handleChange} />
                            <StudentForm 
                                {...selectedUser}
                                isEditing={true}
                                isAdmin={true}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onStudentSubmit = {this.handleEditSubmit} /> 
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

export default AdministrationStudents