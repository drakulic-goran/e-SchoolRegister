import React, { Component } from 'react'
import AdminForm from '../AdminForm/AdminForm'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEdit, faTrash, faUserPlus, faCompactDisc, faEyeSlash, faEye } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/Administration.css'


class AdministrationAdmins extends Component {
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
            activeUserView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/admin/withaccount/', {
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
            const users = allUsers.filter((user) => user.admin.id !== this.state.loggedUser.id)
            const formatedUsers = []
            for (let i = 0; i<users.length; i++) {
                formatedUsers.push({...users[i].admin, userRole: users[i].account.accessRole, username: users[i].account.username})
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

    handleUserClick = (id) => {
        this.state.activeUserView !== id ?
            this.setState({activeUserView: id})
        : this.setState({activeUserView: null})
    }

    renderSubjectData() {
        return this.state.users.map((user, id) => (
            <div className="administration-header" key={user.id}><h2>{user.firstName} {user.lastName}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeUserView !== id ? faEye : faEyeSlash} onClick={() => this.handleUserClick(id)} /></h2>
                <div className={this.state.activeUserView === id ? "" : "hide"}>
                    <span className="user-info"><h4>JMBG</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.jMBG}
                    </span>
                    <span className="user-info"><h4>E-mail</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.email}
                    </span>
                    <span className="user-info"><h4>Gender</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.gender === "GENDER_MALE" ? "Male" : "Female"}
                    </span>
                    <span className="user-info"><h4>Mobile phone number</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {user.mobilePhoneNumber}
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
                                    name="onDelete"
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
        const response = await fetch('http://localhost:8080/project/admin/'+userId, {
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
            this.setState({ users, isLoading: false, activeUserView: null })
            alert("Admin deleted.")
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
        const response = await fetch('http://localhost:8080/project/admin/'+this.state.selectedUser.id, {
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
            alert("Admin changed.")
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

    handleNew = () => { this.setState({newUser: { accessRole: "ROLE_ADMIN" } }); }

    handleNewSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        //console.log(this.state.newUser)
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/admin/', {
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
            const response = await fetch('http://localhost:8080/project/admin/withaccount/', {
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
                const users = allUsers.filter((user) => user.admin.id !== this.state.loggedUser.id)
                const formatedUsers = []
                for (let i = 0; i<users.length; i++) {
                    formatedUsers.push({...users[i].admin, userRole: users[i].account.accessRole, username: users[i].account.username})
                    //console.log(formatedUsers)
                }
                this.setState({ users: formatedUsers, newUser: null, isLoading: false })
                //console.log(this.state.users)
                alert("Admin added.")
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

    handleCancel = () => { this.setState({ selectedUser: null, newUser: null }); }


    render() {
        const {users, selectedUser, newUser} = this.state

        return users.length > 0 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    { !newUser && !selectedUser &&
                        <div>
                            <button type="button"
                                        name="onAddNew"
                                        onClick={this.handleNew}
                                        id="add_btn">
                                <FontAwesomeIcon icon={faUserPlus} />
                                <span> Add new</span>
                            </button>

                            <h1 id='title'>LIST OF ADMINS</h1>

                            <div>
                                {this.renderSubjectData()}
                            </div>
                        </div>
                    }

                    { newUser && 
                        <div>
                            {/*<button type="button"
                                        name="onCancel"
                                        onClick={this.handleCancel}
                                        id="reg_btn">
                                <FontAwesomeIcon icon={faTimes} />
                                <span> Cancel</span>
                    </button>*/}
                            <AdminForm 
                                {...newUser}
                                isEditing={false}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onAdminSubmit = {this.handleNewSubmit} /> 
                        </div>
                    }

                    { selectedUser && 
                        <div>
                            <AdminForm 
                                {...selectedUser}
                                isEditing={true}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onAdminSubmit = {this.handleEditSubmit} /> 
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

export default AdministrationAdmins