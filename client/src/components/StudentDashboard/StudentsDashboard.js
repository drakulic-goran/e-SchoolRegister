import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';
//import PrivateRoute from '../Common/PrivateRoute';
//import NotFoundPage from '../Common/NotFoundPage';
//import StudentProfil from '../StudentProfil/StudentProfil';
//import StudentGrades from '../StudentGrades/StudentGrades';
import '../../Styles/Dashboard.css'
import female from "../../images/avatar-female.jpg";
import male from "../../images/avatar-male.jpg";

//const StudentProfil = lazy(() => import('../StudentProfil/StudentProfil'));
//const StudentGrades = lazy(() => import('../StudentGrades/StudentGrades'));


export class StudentsDashboard extends Component {

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
            const response = await fetch(`http://localhost:8080/project/student/logged`, {
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
                user.enrollmentDate = user.enrollmentDate/*.toLocaleString("sr", {timeZone: "Europe/Belgrade"})*/.split("-").reverse().join("-");
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

    render() {

        //const { loggedUser } = this.state
        const {username, userRole} = this.props

        /*if( isLoading ) {
            return <div>Loading...</div>
        }

        if( loggedUser === null ) {
            return <Redirect to={{ pathname: "/"}} />
        }

        if( isError ) {
            return <div>Error!</div>
        }

        if( isCatchError ) {
        return <div>{this.state.errMsg}!</div>
        }*/

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
                                        <Link to={{ pathname: "/students/studentprofile" }}>edit</Link>
                                    </small>
                                </div>
                        </div>
                    </div>
                </div>

        </React.Suspense>
        
        )
    }
}

export default StudentsDashboard
