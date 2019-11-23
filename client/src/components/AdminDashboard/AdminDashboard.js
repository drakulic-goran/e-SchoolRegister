import React, { Component } from 'react'
//import { Redirect } from 'react-router-dom';
//import AdminForm from '../AdminForm/AdminForm'
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/Dashboard.css'
import female from "../../images/avatar-female.jpg";
import male from "../../images/avatar-male.jpg";



class AdminDashboard extends Component {
 
    constructor(props) {
        super(props)
        this.state = {
            loggedUser: {},
            errMsg: '',
            isCatchError: false,      
            isLoading: false,
            isError: false,
            isLoggedIn: false
        }
    }


    async componentDidMount() {
        this.setState({ isLoading: true })
        if(this.props) {
            const { username, password } = this.props
            const response = await fetch(`http://localhost:8080/project/admin/logged`, {
                mode: 'cors',
                method: 'GET',
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
                const user = await response.json();
                this.setState({ loggedUser: {...user, username, password, confirmedPassword: password }, isLoggedIn:true, isLoading: false })
                this.props.onUserLogged(this.state.loggedUser)
                //console.log(this.state.loggedUser)
            } else if(!this.state.isCatchError) {
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
                //alert("Error: " + err.message)    
                //alert(response.statusText)
                this.setState({ isLoading: false, isError: true })
            }
        } else {
            this.setState({ loggedUser: null, isLoggedIn:false, isLoading: false })
        }

    }

    handleEditUser = (user) => {
        this.setState({ loggedUser: user })
        this.props.onUserLogged(this.state.loggedUser)
    }

    /*constructor(props) {
        super(props)
        this.state = {
            loggedUser: {},
            users: [],
            selectedUser: null,
            savingUser: null,
            isLoading: false,
            isError: false,
            username: '',
            password: '',
            userRole: '',
            isLoggedIn: false
        }
    }


    async componentDidMount() {
        this.setState({ isLoading: true })
        const { username, password, userRole } = this.props
        if(this.props.location.state) {
            const response = await fetch(`http://localhost:8080/project/admin/logged/`, {
                mode: 'cors',
                method: 'GET',
                headers: {
                    'Authorization': 'Basic ' + window.btoa(username + ":" + password),
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8', 
                    'Origin': 'http://localhost:3000', }
                },)
            if( response.ok ) {
                const user = await response.json();
                this.setState({ loggedUser: user, userRole: userRole, username: username, password: password, isLoggedIn:true, isLoading: false })
            } else {
                this.setState({ isLoading: false, isError: true })
            }
        } else {
            this.setState({ loggedUser: null, userRole: null, username: null, password: null, isLoggedIn:false, isLoading: false })
        }

        
    
        this.setState({ isLoading: true })
        const response1 = await fetch('http://localhost:8080/project/admin', {
            mode: 'cors',
            method: 'GET',
            headers: {
                'Authorization': 'Basic ' + window.btoa(username + ":" + password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },)
        if( response1.ok ) {
            const users = await response1.json();
            this.setState({ users, isLoading: false })
        } else {
            this.setState({ isLoading: false, isError: true })
        }
    }*/

    /*renderTableData() {
        return this.state.users.map(user => (
                <tr key={user.id} item={user} />
        ));
    }*/

    /*renderTableHeader() {
        const header = Object.keys(this.state.users[0])
        return header.map((key, index) => {
            return <th key={index}>{key.toUpperCase()}</th>
        })
     }

    renderTableData() {
        return this.state.users.map((user) => {
           return (
              <tr key={user.id}>
                 <td>{user.id}</td>
                 <td>{user.firstName}</td>
                 <td>{user.lastName}</td>
                 <td>{user.jMBG}</td>
                 <td>{user.gender}</td>
                 <td>{user.role}</td>
                 <td>{user.mobilePhoneNumber}</td>
                 <td>{user.email}</td>
                 <td>{user.status}</td>
                 <td>{user.createdById}</td>
                 <td>{user.updatedById}</td>
                 <td><div onClick={ () => this.handleDelete(user.id) }>Delete</div></td>
                 <td><div onClick={ () => this.handleEdit(user.id) }>Edit</div></td>
               </tr>
           )
        })
     }

     handleDelete = async (userId) => {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/admin/'+userId, {
            mode: 'cors',
            method: 'DELETE',
            headers: {
                'Authorization': 'Basic ' + window.btoa(this.state.username + ":" + this.state.password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },)
        if( response.ok ) {
            const users = this.state.users.filter((user) => user.id !== userId)
            this.setState({ users, isLoading: false })
        } else {
            this.setState({ isLoading: false, isError: true })
        }
        //const users = this.state.users.filter((user) => user.id !== userId)
        //this.setState({ users })
    }

    handleEdit = (userId) => { const user = this.state.users.find( us => us.id === userId ); this.setState({ selectedUser: user }); }

    handleEditSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/admin/'+this.state.selectedUser.id, {
            mode: 'cors',
            method: 'PUT',
            body: JSON.stringify(this.state.savingUser),
            headers: {
                'Authorization': 'Basic ' + window.btoa(this.state.username + ":" + this.state.password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },)
        if( response.ok ) {
            const users = [...this.state.users]
            const index = users.findIndex((user) => user.id === this.state.selectedUser.id)
            users.splice(index, 1, this.state.selectedUser)
            this.setState({ users, selectedUser: null, savingUser: null, isLoading: false })
        } else {
            this.setState({ isLoading: false, isError: true })
        }
    }

    handleChange = (e) => {
        e.preventDefault()
        this.setState({
            selectedUser: {
              ...this.state.selectedUser,
              [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
            },
            savingUser: {
            ...this.state.savingUser,
            [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
            }
        })
      }*/


    render() {

        const {username, userRole} = this.props

        return (
            <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>}>
                <div className="profile-info">
                    <div className="header">
                        <h2>Welcome!</h2>
                    </div>
                    <div className="content">
                        <div className="error success" >
                            <h3>Successfully logged in.</h3>
                        </div>
                        <div className="profile_info" style={{float: "left", textAlign: "left"}}>
                            <img src={this.state.loggedUser.gender === "GENDER_MALE" ? male : female} alt="User-face" />
                                <div>
                                    <strong> {username.toUpperCase()} </strong>
                                    <strong>  </strong>
                                    <small>
                                        <i  style={{color: '#888'}}> ({userRole.substr(5).toLowerCase().slice(0,1).toUpperCase() + userRole.substr(5).toLowerCase().slice(1, userRole.substr(5).length)})</i> 
                                    </small>
                                    <br/>
                                    <small>
                                        <Link to={{ pathname: "/admins/adminprofile" }}>edit</Link>
                                    </small>
                                </div>
                        </div>
                    </div>
                </div>
            </React.Suspense>
        )

        /*const {users, selectedUser, isLoading, isError, loggedUser } = this.state
        if( isLoading ) {
            return <div>Loading...</div>
        }

        if( loggedUser === null ) {
            return <Redirect to={{ pathname: "/"}} />
        }

        if( isError ) {
            return <div>Error</div>
        }

        return users.length > 0 
        ? (
            <div>
                <h1 id='title'>LIST OF ADMINS</h1>
                <table id="users">
                    <thead>
                        <tr>{this.renderTableHeader()}</tr>
                    </thead>
                    <tbody>
                        {this.renderTableData()}
                    </tbody>
                </table>
                { selectedUser && 
                    <AdminForm 
                        {...selectedUser}
                        onChange={this.handleChange} 
                        onAdminSubmit = {this.handleEditSubmit}
                    /> 
                }
            </div>
        )
        : null*/

    }
}

export default AdminDashboard