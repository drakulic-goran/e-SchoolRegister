import React, { Component } from 'react'
import TeacherForm from '../TeacherForm/TeacherForm'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEdit, faTrash, faUserPlus, faCompactDisc, faUsers, faEyeSlash, faEye } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/Administration.css'
import AssignUnassignSubjects from '../AssignUnassign/AssignUnassignSubjects';
import AssignUnassignDepartments from '../AssignUnassign/AssignUnassignDepartments';


class AdministrationTeachers extends Component {

    constructor(props) {
        super(props)
        this.state = {
            loggedUser: this.props.loggedUser,
            newUser: null,
            errMsg: '',
            isCatchError: false,
            selectedTeacher: null,
            subjects: [],
            departments: [],
            users: [],
            selectedUser: null,
            savingUser: null,
            selectedSubject: null,
            isLoading: false,
            isError: false,
            activeTeacherView: null
        }
    }


    async componentDidMount() {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/teacher/withaccount/', {
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
            const allUsers = await response.json();
            const users = allUsers.filter((user) => user.user.id !== this.state.loggedUser.id)
            users.forEach((user)=>{ user.user.employmentDate = user.user.employmentDate.split("-").reverse().join("-"); })
            const formatedUsers = []
            for (let i = 0; i<users.length; i++) {
                formatedUsers.push({...users[i].user, teachingDepartments: users[i].teachingSubjectDepartments, userRole: users[i].account.accessRole, username: users[i].account.username})
            }

            const response1 = await fetch('http://localhost:8080/project/subjects/', {
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
                const allSubjects = await response1.json();
                //console.log(allSubjects)
                this.setState({ subjects: allSubjects })
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

            //console.log(formatedUsers)
            this.setState({ users: formatedUsers, isLoading: false })
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
    }

    shouldComponentUpdate(nextProps, nextState) {
        if (this.state !== nextState) {
            return true;
        }
        return false;
      }

    handleTeachersClick = (id) => {
        this.state.activeTeacherView !== id ?
            this.setState({activeTeacherView: id})
        : this.setState({activeTeacherView: null})
    }

    renderSubjectData() {
        return this.state.users.map((user, id) => (
            <div className="administration-header" key={user.id}><h2>{user.firstName} {user.lastName}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeTeacherView !== id ? faEye : faEyeSlash} onClick={() => this.handleTeachersClick(id)} /></h2>
                <div className={this.state.activeTeacherView === id ? "" : "hide"}>
                    <div className="administration-assign">Assigned subjects: 
                        {user.teachingDepartments.map((subject, id1) =>
                        <span key={id1}>
                            <div className="user-info cursor-pointer" key={subject.subject.id} onClick={ () => this.handleEditDepartments(user.id, subject.subject.id) } > {subject.subject.subjectName}
                                {subject.teachingDepartments.map((department, id2) =>
                                <span key={department.teachingDepartment.id}> {department.teachingClass.classLabel}-{department.teachingDepartment.departmentLabel}</span>
                                )}
                            </div>
                        </span> )}
                        <button type="button"
                                    name="onChangeSubjects"
                                    onClick={ () => this.handleEditSubjects(user.id) }
                                    id="change_btn">
                            <FontAwesomeIcon icon={faUsers} /> 
                            <span> Change</span>
                        </button>
                    </div>
                    <span className="user-info"><h4>JMBG</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.jMBG}
                    </span>
                    <span className="user-info"><h4>Certificate</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.certificate}
                    </span>
                    <span className="user-info"><h4>Employment date</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.employmentDate}
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
        const response = await fetch('http://localhost:8080/project/teacher/'+userId, {
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
            this.setState({ users, isLoading: false, activeTeacherView: null })
            alert("Teacher deleted.")
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
        const response = await fetch('http://localhost:8080/project/teacher/'+this.state.selectedUser.id, {
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
            const users = [...this.state.users]
            const index = users.findIndex((user) => user.id === this.state.selectedUser.id)
            users.splice(index, 1, this.state.selectedUser)
            this.setState({ users, selectedUser: null, savingUser: null, isLoading: false })
            alert("Teacher changed.")
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

    handleNew = () => { this.setState({newUser: { accessRole: "ROLE_TEACHER" } }); }

    handleNewSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/teacher/', {
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
            const response = await fetch('http://localhost:8080/project/teacher/withaccount/', {
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
                const allUsers = await response.json();
                const users = allUsers.filter((user) => user.user.id !== this.state.loggedUser.id)
                users.forEach((user)=>{ user.user.employmentDate = user.user.employmentDate.split("-").reverse().join("-"); })
                const formatedUsers = []
                for (let i = 0; i<users.length; i++) {
                    formatedUsers.push({...users[i].user, teachingDepartments: users[i].teachingSubjectDepartments, userRole: users[i].account.accessRole, username: users[i].account.username})
                }
                this.setState({ users: formatedUsers, newUser: null, isLoading: false })
                alert("Teaacher added.")
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

    handleCancel = () => { this.setState({ selectedUser: null, newUser: null, selectedTeacher: null, selectedSubject:null }); }

    handleEditSubjects = (userId) => { 
        const user = this.state.users.find( us => us.id === userId ); 
        //console.log(user)
        const teacherSubjects = [...user.teachingDepartments]
        let subjects = []
        teacherSubjects.forEach(sub => subjects = [...subjects, sub.subject])
        this.setState({ selectedTeacher: {...user, subjects: subjects, students: subjects} }); 
    }

    handleSubmitSubjects = async (subjects) => {
        this.setState({ isLoading: true })
        //console.log(this.state.selectedTeacher)
        //console.log(subjects)
        //let subjectsRemove = this.state.selectedTeacher.subjects.filter(x => !subjects.includes(x));
        let subjectsRemove = this.state.selectedTeacher.subjects.filter(x => !subjects.some(item => item.id === x.id ));
        //let subjectsAdd = subjects.filter(x => !this.state.selectedTeacher.subjects.includes(x));
        let subjectsAdd = subjects.filter(x => !this.state.selectedTeacher.subjects.some(item => item.id === x.id ));
        //console.log(subjectsRemove)
        //console.log(subjectsAdd)
        let tempRemove = []
        let tempAdd = []
        for (let i = 0; i<subjectsRemove.length; i++) {
            tempRemove = [...tempRemove, `${subjectsRemove[i].id}`]
        }
        for (let j = 0; j<subjectsAdd.length; j++) {
            tempAdd = [...tempAdd, `${subjectsAdd[j].id}`]
        }
        if (subjectsRemove.length > 0 || subjectsAdd.length > 0) {
            if (tempRemove.length > 0) {
                //console.log(tempRemove)
                const response = await fetch('http://localhost:8080/project/teacher/'+this.state.selectedTeacher.id+'/remove-subject/', {
                    mode: 'cors',
                    method: 'PUT',
                    body: JSON.stringify({subjects: [...tempRemove]}),
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
                    let remainSubjects = [...this.state.selectedTeacher.teachingDepartments]
                    subjectsRemove.forEach(removedSubject => remainSubjects = remainSubjects.filter((sub) => sub.subject.id !== removedSubject.id))               
                    /*let remainFormatedSubjects = []
                    remainSubjects.forEach(sub => remainFormatedSubjects = [...remainFormatedSubjects, {subject: sub}])
                    //console.log(remainFormatedSubjects)*/
                    this.setState({ selectedTeacher: {...this.state.selectedTeacher, teachingDepartments: remainSubjects} })
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
            if (tempAdd.length > 0) {
                //console.log(tempAdd)
                const response = await fetch('http://localhost:8080/project/teacher/'+this.state.selectedTeacher.id+'/add-subject/', {
                    mode: 'cors',
                    method: 'PUT',
                    body: JSON.stringify({subjects: [...tempAdd]}),
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
                    //console.log(subjectsAdd)
                    let remainFormatedSubjects = []
                    subjectsAdd.forEach(sub => remainFormatedSubjects = [...remainFormatedSubjects, {subject: sub, teachingDepartments: [] }])

                    let remainSubjects = [...this.state.selectedTeacher.teachingDepartments, ...remainFormatedSubjects]
                    //console.log(remainSubjects)

                    /*let remainFormatedSubjects = []
                    remainSubjects.forEach(sub => remainFormatedSubjects = [...remainFormatedSubjects, {subject: sub}])*/
                    this.setState({ selectedTeacher: {...this.state.selectedTeacher, teachingDepartments: remainSubjects} })
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
            const users = [...this.state.users]
            const index = users.findIndex((user) => user.id === this.state.selectedTeacher.id)
            users.splice(index, 1, this.state.selectedTeacher)
            this.setState({ users, selectedTeacher: null, isLoading: false })
            alert("Subjects for teacher changed.")
        }
        else {
            alert("No changes submited.")
            this.setState({ selectedTeacher: null, isLoading: false })
        }
    }
  
    handleEditDepartments = (userId, subjectId) => { 
        const user = this.state.users.find( us => us.id === userId )
        //console.log(user)
        const teacherSubjectDepartments = [...user.teachingDepartments]
        const subject = teacherSubjectDepartments.find( sub => sub.subject.id === subjectId )
        let alldepartments = []
        subject.subject.classes.forEach(sub => 
            sub.clas.departments.forEach(dep => 
                alldepartments = [...alldepartments, {...dep.department, clas: sub.clas} ]))

        const allAlivedepartments = alldepartments.filter((department) => department.status === 1)

        
        let teachingDepartments = []
        subject.teachingDepartments.forEach(sub => 
                teachingDepartments = [...teachingDepartments, {...sub.teachingDepartment, clas: sub.teachingClass} ])
        this.setState({ departments: [...allAlivedepartments], selectedSubject: {...subject, selectedTeacher: {...user}, alldepartments: [...allAlivedepartments], teachingDepartments: [...teachingDepartments], students: [...teachingDepartments] } })
    }

    handleSubmitDepartments = async (subjects) => {
        this.setState({ isLoading: true })
        let subjectsRemove = this.state.selectedSubject.teachingDepartments.filter(x => !subjects.some(item => item.id === x.id ));
        let subjectsAdd = subjects.filter(x => !this.state.selectedSubject.teachingDepartments.some(item => item.id === x.id ));
        let tempRemove = {teachingDepartments: []}
        let tempAdd = {teachingDepartments: []}
        for (let i = 0; i<subjectsRemove.length; i++) {
            tempRemove = { teachingDepartments: [...tempRemove.teachingDepartments, `${subjectsRemove[i].id}`], teachingSubject: `${this.state.selectedSubject.subject.id}`, schoolYear: "2018-2019" }
        }
        for (let j = 0; j<subjectsAdd.length; j++) {
            tempAdd = { teachingDepartments: [...tempAdd.teachingDepartments, `${subjectsAdd[j].id}`], teachingSubject: `${this.state.selectedSubject.subject.id}`, schoolYear: "2018-2019" }
        }
        if (subjectsRemove.length > 0 || subjectsAdd.length > 0) {
            if (subjectsRemove.length > 0) {
                const response = await fetch('http://localhost:8080/project/teacher/'+this.state.selectedSubject.selectedTeacher.id+'/remove-departments-from-subject/', {
                    mode: 'cors',
                    method: 'PUT',
                    body: JSON.stringify(tempRemove),
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
                    const removedDepartments = await response.json();
                    //console.log(removedDepartments)
                    //console.log(this.state.selectedSubject.selectedTeacher)
                    const teacherSubjectsWithDepartments = [...this.state.selectedSubject.selectedTeacher.teachingDepartments]
                    //console.log(teacherSubjectsWithDepartments)
                    const remainDepartments = teacherSubjectsWithDepartments.find( sub => `${sub.subject.id}` === tempRemove.teachingSubject )
                    //console.log(remainDepartments)
                    removedDepartments.forEach(removedDepartment => remainDepartments.teachingDepartments = remainDepartments.teachingDepartments.filter((dep) => dep.id !== removedDepartment.id))               
                    //console.log(remainDepartments)
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
            if (subjectsAdd.length > 0) {
                const response = await fetch('http://localhost:8080/project/teacher/'+this.state.selectedSubject.selectedTeacher.id+'/add-departments-to-subject/', {
                    mode: 'cors',
                    method: 'PUT',
                    body: JSON.stringify(tempAdd),
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
                    const addedDepartments = await response.json();
                    //console.log(addedDepartments)
                    //console.log(this.state.selectedSubject.selectedTeacher)
                    const teacherSubjectsWithDepartments = [...this.state.selectedSubject.selectedTeacher.teachingDepartments]
                    //console.log(teacherSubjectsWithDepartments)
                    let remainDepartments = teacherSubjectsWithDepartments.find( sub => `${sub.subject.id}` === tempAdd.teachingSubject ) ? teacherSubjectsWithDepartments.find( sub => `${sub.subject.id}` === tempAdd.teachingSubject ) : []
                    //console.log(remainDepartments)
                    remainDepartments.teachingDepartments = [...remainDepartments.teachingDepartments, ...addedDepartments]
                    //console.log(remainDepartments)
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
            const users = [...this.state.users]
            const index = users.findIndex((user) => user.id === this.state.selectedSubject.selectedTeacher.id)
            users.splice(index, 1, this.state.selectedSubject.selectedTeacher)
            this.setState({ users, selectedSubject: null, isLoading: false })
            alert("Departments for subject changed.")
        }
        else {
            alert("No changes submited.")
            this.setState({ selectedTeacher: null, isLoading: false })
        }
    }


    render() {

        const {users, selectedUser, newUser, selectedTeacher, subjects, selectedSubject, departments} = this.state

        return users.length > 0 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    { !newUser && !selectedUser && !selectedTeacher && !selectedSubject &&
                        <div>
                            <button type="button"
                                        name="onAddNew"
                                        onClick={this.handleNew}
                                        id="add_btn">
                                <FontAwesomeIcon icon={faUserPlus} />
                                <span> Add new</span>
                            </button>

                            <h1 id='title'>LIST OF TEACHERS</h1>

                            <div>
                                {this.renderSubjectData()}
                            </div>
                        </div>
                    }

                    { selectedTeacher &&
                        <div>
                            <h1>Subjects assignments</h1>
                            <AssignUnassignSubjects students={[...subjects]} parent={{...selectedTeacher}} name={selectedTeacher.firstName + " " + selectedTeacher.lastName} editChildren={this.handleSubmitSubjects} onCancel={this.handleCancel} />
                        </div>
                    }

                    { selectedSubject &&
                        <div>
                            <h1>Departments assignments</h1>
                            <AssignUnassignDepartments students={[...departments]} parent={{...selectedSubject}} name={selectedSubject.subject.subjectName} editChildren={this.handleSubmitDepartments} onCancel={this.handleCancel} />
                        </div>
                    }

                    { newUser && 
                        <div>
                            <TeacherForm 
                                {...newUser}
                                isEditing={false}
                                isAdmin={true}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleNewSubmit} /> 
                        </div>
                    }

                    { selectedUser && 
                        <div>
                            <TeacherForm 
                                {...selectedUser}
                                isEditing={true}
                                isAdmin={true}
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

export default AdministrationTeachers