import React, {Component, lazy} from 'react';
import { Redirect } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';
import 'moment-timezone';
import '../../Styles/EditUser.css';

const TeacherForm = lazy(() => import('../TeacherForm/TeacherForm'));


class StudentProfil extends Component {

    constructor(props) {
        super(props)
        this.state = {
            loggedUser: this.props.loggedUser,
            editedUser: null,
            errMsg: '',
            isCatchError: false,      
            isLoading: false,
            isError: false
        }
    }

    handleDelete = async () => {
        this.setState({ isLoading: true })
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/teacher/'+this.props.loggedUser.id, {
            mode: 'cors',
            method: 'DELETE',
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
            this.setState({ loggedUser: null, editedUser: null, isLoading: false })
            this.props.onEditUser(this.state.loggedUser)
            alert("User deleted. You will be logout now!")
            window.location.reload();
        } else if (!this.state.isCatchError) {
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
    }

    handleEdit = async (e) => {
        if(this.state.editedUser!==null) {
            this.setState({ isLoading: true })
            const { username, password } = this.props
            const response = await fetch('http://localhost:8080/project/teacher/'+this.props.loggedUser.id, {
                mode: 'cors',
                method: 'PUT',
                body: JSON.stringify(this.state.editedUser),
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
                const userAccount = await response.json();
                const user = {...userAccount.user};
                user.employmentDate = user.employmentDate.split("-").reverse().join("-");
                let un
                let pw
                this.state.editedUser.username ? un=this.state.editedUser.username : un=username
                this.state.editedUser.password ? pw=this.state.editedUser.password : pw=password
                this.setState({ loggedUser: {...user, username: un, password: pw, confirmedPassword: pw }, editedUser: null, isLoading: false })
                this.props.onEditUser(this.state.loggedUser)
                alert("User data changed.")
            } else if (!this.state.isCatchError) {
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
            alert("No changes submited.")
        }
    }
    
    handleChange = (e) => {
        this.setState({
            editedUser: {
                ...this.state.editedUser,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
            }
        })
    }

    handleCancel = () => { this.props.history.goBack() }


    render() {

        const { loggedUser } = this.state

        /*if( isError ) {
            return <div>Error!</div>
        }
        if( isCatchError ) {
        return <div>{this.state.errMsg}!</div>
        }*/

        return loggedUser 
        ? (
            <div>
                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>
                    
                    { loggedUser && 
                            <TeacherForm 
                                {...loggedUser}
                                onEdit={this.handleLoggedUser} 
                                isEditing={true}
                                onChange={this.handleChange}
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleEdit}
                                id='sf'
                            /> 
                    }

                </React.Suspense>

            </div>
        ) : <Redirect to={{ pathname: "/"}} />
    }
}
 
export default StudentProfil