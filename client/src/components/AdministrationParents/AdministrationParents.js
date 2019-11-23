import React, { Component } from 'react'
import ParentForm from '../ParentForm/ParentForm'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEdit, faTrash, faUserPlus, faCompactDisc, faUsers, faEyeSlash, faEye } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/Administration.css'
import AssignUnassign from '../AssignUnassign/AssignUnassign';


class AdministrationParents extends Component {
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
            students: [],
            activeParentView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/parent/withaccount/', {
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
            const formatedUsers = []
            for (let i = 0; i<users.length; i++) {
                formatedUsers.push({...users[i].user, userRole: users[i].account.accessRole, username: users[i].account.username})
            }


            const response1 = await fetch('http://localhost:8080/project/student/', {
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
                const allStudents = await response1.json();
                this.setState({ students: allStudents })
                //console.log(this.state.students)
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

            this.setState({ users: formatedUsers, isLoading: false })
            //console.log(this.state.users)
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

    handleParentClick = (id) => {
        this.state.activeParentView !== id ?
            this.setState({activeParentView: id})
        : this.setState({activeParentView: null})
    }


    renderSubjectData() {
        return this.state.users.map((user, id) => (
            <div className="administration-header" key={user.id}><h2>{user.firstName} {user.lastName}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeParentView !== id ? faEye : faEyeSlash} onClick={() => this.handleParentClick(id)} /></h2>
                <div className={this.state.activeParentView === id ? "" : "hide"}>
                    <div className="administration-assign">Children: 
                        {user.students.map((student, id1) => (
                            student.status === 1 ? 
                                <span className="user-info" key={student.id}>
                                    {student.firstName} {student.lastName}
                                </span>
                                : null
                        ))}
                        <button type="button"
                                    name="onEditChildren"
                                    onClick={ () => this.handleEditChildren(user.id) }
                                    id="change_btn">
                            <FontAwesomeIcon icon={faUsers} /> 
                            <span> Change</span>
                        </button>
                    </div>
                    <span className="user-info"><h4>JMBG</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.jMBG}
                    </span>
                    <span className="user-info"><h4>E-mail</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.email}
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
        const response = await fetch('http://localhost:8080/project/parent/'+userId, {
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
            this.setState({ users, isLoading: false, activeParentView: null })
            alert("Parent deleted.")
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
        //console.log(this.state.selectedUser)
        const response = await fetch('http://localhost:8080/project/parent/'+this.state.selectedUser.id, {
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
            alert("Parent changed.")
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

    handleNew = () => { this.setState({newUser: { accessRole: "ROLE_PARENT" } }); }

    handleNewSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        //console.log(this.state.newUser)
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/parent/', {
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
            const response = await fetch('http://localhost:8080/project/parent/withaccount/', {
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
                const formatedUsers = []
                for (let i = 0; i<users.length; i++) {
                    formatedUsers.push({...users[i].user, userRole: users[i].account.accessRole, username: users[i].account.username})
                    //console.log(formatedUsers)
                }
                this.setState({ users: formatedUsers, newUser: null, isLoading: false })
                //console.log(this.state.users)
                alert("Parent added.")
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

    handleCancel = () => { this.setState({ selectedUser: null, newUser: null, selectedParent: null }); }

    handleEditChildren = (userId) => { 
        const user = this.state.users.find( us => us.id === userId ); 
        //console.log(user)
        this.setState({ selectedParent: {...user} }); 
    }

    handleSubmitChildren = async (children) => {
        this.setState({ isLoading: true })
        let childrenRemove = this.state.selectedParent.students.filter(x => !children.includes(x));
        let childrenAdd = children.filter(x => !this.state.selectedParent.students.includes(x));
        //console.log(childrenRemove)
        //console.log(childrenAdd)
        if (childrenRemove.length > 0 || childrenAdd.length > 0) {
            if (childrenRemove.length > 0) {
                for (let i = 0; i<childrenRemove.length; i++) {
                    const child = childrenRemove[i]
                    const response = await fetch('http://localhost:8080/project/parent/'+this.state.selectedParent.id+'/remove/child/'+child.id, {
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
                    if( !this.state.isCatchError && response.ok ) {
                        /*let selectedParent = await response.json();
                        selectedParent = {...selectedParent, userRole: this.state.selectedParent.accessRole, username: this.state.selectedParent.username }*/
                        const children = [...this.state.selectedParent.students]
                        const remainChildren = children.filter((user) => user.id !== child.id)                
                        this.setState({ selectedParent: {...this.state.selectedParent, students: remainChildren} })
                        /*const users = [...this.state.users]
                        const index = users.findIndex((user) => user.id === selectedParent.id)
                        users.splice(index, 1, selectedParent)
                        this.setState({ users })*/
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
                    //console.log("REMOVE")
                    //console.log(this.state.selectedParent)
                }

            }
            if (childrenAdd.length > 0) {
                for (let j = 0; j<childrenAdd.length; j++) {
                    const child = childrenAdd[j]
                    const response = await fetch('http://localhost:8080/project/parent/'+this.state.selectedParent.id+'/child/'+child.id, {
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
                    if( !this.state.isCatchError && response.ok ) {
                        /*let selectedParent = await response.json();
                        selectedParent = {...selectedParent, userRole: this.state.selectedParent.accessRole, username: this.state.selectedParent.username }*/
                        const children = [...this.state.selectedParent.students]
                        const remainChildren = [...children, child]                        
                        this.setState({ selectedParent: {...this.state.selectedParent, students: remainChildren} })
                        /*const users = [...this.state.users]
                        const index = users.findIndex((user) => user.id === selectedParent.id)
                        users.splice(index, 1, selectedParent)
                        this.setState({ users })*/
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
                //console.log("ADD")
                //console.log(this.state.selectedParent)    
            }
            const users = [...this.state.users]
            const index = users.findIndex((user) => user.id === this.state.selectedParent.id)
            users.splice(index, 1, this.state.selectedParent)
            this.setState({ users })
            alert("Children changed.")
        }
        else {
            alert("No changes submited.")
        }
        this.setState({ selectedParent: null, isLoading: false }); 
    }


    render() {

        const {users, selectedUser, newUser, selectedParent, students} = this.state

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

                            <h1 id='title'>LIST OF PARENTS</h1>

                            <div>
                                {this.renderSubjectData()}
                            </div>
                        </div>
                    }

                    { selectedParent &&
                        <div>
                            <h1>Children assignments</h1>
                            <AssignUnassign 
                                students={[...students]} 
                                parent={{...selectedParent}} 
                                name={selectedParent.firstName + " " + selectedParent.lastName} 
                                editChildren={this.handleSubmitChildren} 
                                onCancel={this.handleCancel} />
                        </div>
                    }

                    { newUser && 
                        <div>
                            <ParentForm 
                                {...newUser}
                                isEditing={false}
                                isAdmin={true}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onParentSubmit = {this.handleNewSubmit} /> 
                        </div>
                    }

                    { selectedUser && 
                        <div>
                            <ParentForm 
                                {...selectedUser}
                                isEditing={true}
                                isAdmin={true}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onParentSubmit = {this.handleEditSubmit} /> 
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

export default AdministrationParents