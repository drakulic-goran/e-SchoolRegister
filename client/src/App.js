import React, {Component, lazy} from 'react';
import {BrowserRouter, Switch, Route, Redirect, withRouter, Link } from 'react-router-dom';
import { CSSTransition, TransitionGroup } from 'react-transition-group'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars, faUser, faChevronDown, faSignOutAlt, faSignInAlt, faColumns, faSpinner, faTools, faUserGraduate, faChalkboardTeacher, faUserCog, faBookOpen, faChalkboard, faEdit, faUsers, faFileArchive } from '@fortawesome/free-solid-svg-icons';
import './Styles/Style.css';
import './Styles/CSSTransition.css'
import WatchOutsideEvent from './components/Common/WatchOutsideEvent';
import PrivateRoute from './components/Common/PrivateRoute';
import LoginPage from './components/LoginForm/LoginForm';
import NotFoundPage from './components/Common/NotFoundPage';
//import StudentsDashboard from './components/StudentDashboard/StudentsDashboard';
//import TeacherDashboard from './components/TeacherDashboard/TeacherDashboard';
//import ParentDashboard from './components/ParentDashboard/ParentDashboard';
//import AdminDashboard from './components/AdminDashboard/AdminDashboard';
import './App.css';
//import logo from './images/Logo2.png';

const StudentsDashboard = lazy(() => import('./components/StudentDashboard/StudentsDashboard'));
const TeacherDashboard = lazy(() => import('./components/TeacherDashboard/TeacherDashboard'));
const ParentDashboard = lazy(() => import('./components/ParentDashboard/ParentDashboard'));
const AdminDashboard = lazy(() => import('./components/AdminDashboard/AdminDashboard'));
const StudentGrades = lazy(() => import('./components/StudentGrades/StudentGrades'));
const StudentProfil = lazy(() => import('./components/StudentProfil/StudentProfil'));
const ParentProfil = lazy(() => import('./components/ParentProfil/ParentProfil'));
const ParentGrades = lazy(() => import('./components/ParentGrades/ParentGrades'));
const TeacherProfil = lazy(() => import('./components/TeacherProfil/TeacherProfil'));
const TeacherGrades = lazy(() => import('./components/TeacherGrades/TeacherGrades'));
const TeacherSubjects = lazy(() => import('./components/TeacherSubjects/TeacherSubjects'));
const AdminProfil = lazy(() => import('./components/AdminProfil/AdminProfil'));
const AdministrationAdmins = lazy(() => import('./components/AdministrationAdmins/AdministrationAdmins'));
const AdministrationParents = lazy(() => import('./components/AdministrationParents/AdministrationParents'));
const AdministrationSubjects = lazy(() => import('./components/AdministrationSubjects/AdministrationSubjects'));
const AdministrationClasses = lazy(() => import('./components/AdministrationClasses/AdministrationClasses'));
const AdministrationDepartments = lazy(() => import('./components/AdministrationDepartments/AdministrationDepartments'));
const AdministrationStudents = lazy(() => import('./components/AdministrationStudents/AdministrationStudents'));
const AdministrationGrades = lazy(() => import('./components/AdministrationGrades/AdministrationGrades'));
const AdministrationTeachers = lazy(() => import('./components/AdministrationTeachers/AdministrationTeachers'));


const TermsAndConditions = lazy(() => import('./Pages/TermsAndConditions'));
const PrivacyPolicy = lazy(() => import('./Pages/PrivacyPolicy'));

var saveAs = require('file-saver');

class App extends Component {

  constructor(props) {
    super(props)
    this.state = {
      username: '',
      password: '',
      user: {},
      userRole: '',
      linked: '/',
      errMsg: '',
      isCatchError: false,
      isLoggedIn: false,
      isLoading: false, 
      isError: false,
      isShowingFaBarsMenu: false,
      isShowingFaBarsMenuChck: false,
      isShowingSubmenu: false,
      isShowingSubmenuAdministration: false,
      isShowingSubmenuTeacher: false
    }
  }


  handleLoginChange = (e) => {
    this.setState({
    [e.target.name]: e.target.value
    })
  }

  handleLogin = async (e) => {
    this.setState({isLoading: true})
    const response = await fetch('http://localhost:8080/project/users/LogIn', {
      mode: 'cors',
      method: 'GET',
      headers: {
          'Authorization': 'Basic ' + window.btoa(this.state.username + ":" + this.state.password),
          'Accept': 'application/json; charset=UTF-8',
          'Content-Type': 'application/json; charset=UTF-8', 
          'Origin': 'http://localhost:3000', }
      },).catch((error) => {
        this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
        alert(error.toString().substr(4) + "!")
        //this.setState({ isLoading: false, username: '', password: '' })
        window.location.reload();
    })
    if( !this.state.isCatchError && response.ok ) {
        const userAccount = await response.json();
        const user = {...userAccount.user, userRole: userAccount.accessRole, username: this.state.username, password: this.state.password };
        //user.enrollmentDate = user.enrollmentDate.split("-").reverse().join("-");
        switch(userAccount.accessRole) {
          case "ROLE_ADMIN": 
                    this.setState({ linked:"/admins"})
                    break
          case "ROLE_TEACHER": 
                    this.setState({ linked:"/teachers"})
                    break
          case "ROLE_PARENT": 
                    this.setState({ linked:"/parents"})
                    break
          case "ROLE_STUDENT": 
                    this.setState({ linked:"/students"})
                    break
          default:
                    this.setState({ linked:"/"})
                    break
        }
        this.setState({ user: user, userRole: userAccount.accessRole, isLoggedIn:true, isLoading: false })
        //console.log(this.state.user)
    } else if( !this.state.isCatchError && response.status === 401 ) {
        alert("Wrong username or password!");
        //this.setState({ isLoading: false, username: '', password: '' })
        //window.location.reload();
    } else if (!this.state.isCatchError) {
        const err = await response.json()
        alert("Error: " + err.message)    
        //alert(response.statusText)
        //this.setState({ isLoading: false, isError: true })
        window.location.reload();
    }
  }

  handleLogOut = (e) => {
    //this.setState({isLoggedIn: false, user: {}, userRole: '', username: '', password: '', linked:"/" })
    alert("Successfully logged out.");
    window.location.reload();
    e.preventDefault()
  }

  toggleFaBars = (e) => {
    this.state.isShowingFaBarsMenuChck ? this.setState({isShowingFaBarsMenu: false, isShowingFaBarsMenuChck: false }) : this.setState({isShowingFaBarsMenu: true, isShowingFaBarsMenuChck: true })
  }
  
  toggleSubmenuOnHover = (e) => {
    this.state.isShowingSubmenu ? this.setState({isShowingSubmenu: false }) : this.setState({isShowingSubmenu: true })
  }

  toggleSubmenuAdministrationOnHover = (e) => {
    this.state.isShowingSubmenuAdministration ? this.setState({isShowingSubmenuAdministration: false }) : this.setState({isShowingSubmenuAdministration: true })
  }

  toggleSubmenuTeacherOnHover = (e) => {
    this.state.isShowingSubmenuTeacher ? this.setState({isShowingSubmenuTeacher: false }) : this.setState({isShowingSubmenuTeacher: true })
  }

  handleClickOutside = () => {
    this.setState({ isShowingSubmenuTeacher: false, isShowingSubmenuAdministration: false, isShowingSubmenu: false, isShowingFaBarsMenu: false })
  }

  handleMouseLeaveOutsideHeader = () => {
  	this.setState({ isShowingSubmenuTeacher: false, isShowingSubmenuAdministration: false, isShowingSubmenu: false, isShowingFaBarsMenu: false });
  }

  handleMouseLeaveOutsideSubmenu = () => {
  	this.setState({ isShowingSubmenu: false });
  }

  handleMouseLeaveOutsideSubmenuAdministration = () => {
  	this.setState({ isShowingSubmenuAdministration: false });
  }

  handleMouseLeaveOutsideSubmenuTeacher = () => {
  	this.setState({ isShowingSubmenuTeacher: false });
  }

  handleUpdateLoggedUser = (user) => {
    this.setState({ user: user, username: user.username, password: user.password,})
    //console.log(this.state.user)
  }

  handleGetLogFile = async () => {
    fetch('http://localhost:8080/project/download/download/', {
      mode: 'cors',
      method: 'GET',
      headers: {
          'Authorization': 'Basic ' + window.btoa(this.state.username + ":" + this.state.password),
          'Accept': 'application/json; charset=UTF-8',
          'Content-Type': 'text/csv', 
          'Origin': 'http://localhost:3000', 
        },
      responseType: 'blob'
    }).then(response => response.blob())
    .then(blob => saveAs(blob, 'logFile.txt'));
  }


  render () {
    
    //const { isError, isCatchError } = this.state

    /*if( isLoading ) {
        return <div><FontAwesomeIcon icon={faSpinner} spin /></div>
    }*/

    /*if( isError ) {
      return <div>Error!</div>
    }

    if( isCatchError ) {
      return <div>{this.state.errMsg}!</div>
    }*/


    return (
      
      <div>

        <TransitionGroup>
          <CSSTransition
              classNames="homeTransition"
              appear
              timeout = {500}
              unmountOnExit
              enter = {false}
              exit = {false}  >

            <BrowserRouter>
              <>

                <WatchOutsideEvent onClickOutside={this.handleClickOutside} onMouseLeaveOutside={this.handleMouseLeaveOutsideHeader}>
                  <header className={ this.state.isLoggedIn ? "show showing" : "hide" } >
                      <div className="logo">
                        <h1 className="logo-text"><span>e-Class</span>Register</h1>
                      </div>
                      <FontAwesomeIcon icon={faBars} className="menu-toggle" onClick={() => this.toggleFaBars()} />
                      <ul className={"nav " + (this.state.isShowingFaBarsMenu ? "showing" : null)} >
                        {/*<li>
                          <Link to={this.state.linked}>
                            <FontAwesomeIcon icon={faHome} />
                            <span> Home</span>
                          </Link>
                        </li>*/}
                        {!this.state.isLoggedIn ? (
                          <li>
                            <Link to={"/"}>
                              <FontAwesomeIcon icon={faSignInAlt} />
                              <span> Login</span>
                            </Link>
                          </li>
                        ) : null }


                        <WatchOutsideEvent onClickOutside={this.handleClickOutside} onMouseLeaveOutside={!this.state.isShowingFaBarsMenu ? this.handleMouseLeaveOutsideSubmenuAdministration : null}>
                          {this.state.isLoggedIn && this.state.userRole === "ROLE_ADMIN" ? (
                            <li>
                              <div onMouseEnter={() => this.toggleSubmenuAdministrationOnHover()} >
                                <FontAwesomeIcon icon={faTools} />
                                <span> Administration </span>
                                <FontAwesomeIcon icon={faChevronDown} size="xs" />
                              </div>
                              <ul className={(this.state.isShowingSubmenuAdministration ? "showing" : null)} onMouseLeave={() => (!this.state.isShowingFaBarsMenu ? this.toggleSubmenuOnHover() : null)} >
                                <li>
                                  <Link to={this.state.linked + "/adminsadministration"}>
                                    <FontAwesomeIcon icon={faUserCog} />
                                    <span> Admins</span>
                                  </Link>
                                </li>                  
                                <li>
                                  <Link to={this.state.linked + "/teachersadministration"}>
                                    <FontAwesomeIcon icon={faChalkboardTeacher} />
                                    <span> Teachers</span>
                                  </Link>
                                </li>                  
                                <li>
                                  <Link to={this.state.linked + "/parentsadministration"}>
                                    <FontAwesomeIcon icon={faUser} />
                                    <span> Parents</span>
                                  </Link>
                                </li>     
                                <li>
                                  <Link to={this.state.linked + "/studentsadministration"}>
                                    <FontAwesomeIcon icon={faUserGraduate} />
                                    <span> Students</span>
                                  </Link>
                                </li>         
                                <li>
                                  <Link to={this.state.linked + "/classesadministration"}>
                                    <FontAwesomeIcon icon={faChalkboard} />
                                    <span> Classes</span>
                                  </Link>
                                </li>   
                                <li>
                                  <Link to={this.state.linked + "/departmentsadministration"}>
                                    <FontAwesomeIcon icon={faUsers} />
                                    <span> Departments</span>
                                  </Link>
                                </li>         
                                <li>
                                  <Link to={this.state.linked + "/subjectsadministration"}>
                                    <FontAwesomeIcon icon={faBookOpen} />
                                    <span> Subjects</span>
                                  </Link>
                                </li>         
                                <li>
                                  <Link to={this.state.linked + "/gradesadministration"}>
                                    <FontAwesomeIcon icon={faEdit} />
                                    <span> Grades</span>
                                  </Link>
                                </li>               
                              </ul>
                            </li>
                          ) : null }
                        </WatchOutsideEvent>


                        <WatchOutsideEvent onClickOutside={this.handleClickOutside} onMouseLeaveOutside={!this.state.isShowingFaBarsMenu ? this.handleMouseLeaveOutsideSubmenuTeacher : null}>
                          {this.state.isLoggedIn && this.state.userRole === "ROLE_TEACHER" ? (
                            <li>
                              <Link to={this.state.linked + "/teachersubjects"} onMouseEnter={() => this.toggleSubmenuTeacherOnHover()} >
                                <FontAwesomeIcon icon={faBookOpen} />
                                  <span> Subjects </span>
                                <FontAwesomeIcon icon={faChevronDown} size="xs" />
                              </Link>
                              <ul className={(this.state.isShowingSubmenuTeacher ? "showing" : null)} onMouseLeave={() => !this.state.isShowingFaBarsMenu ? this.toggleSubmenuOnHover() : null} >
                                <li>
                                  <Link to={this.state.linked + "/teachergrades"}>
                                    <FontAwesomeIcon icon={faEdit} />
                                    <span> Grades</span>
                                  </Link>
                                </li>                  
                              </ul>
                            </li>
                          ) : null }
                        </WatchOutsideEvent>



                        { (this.state.isLoggedIn && this.state.userRole === "ROLE_PARENT") ? (
                          <li>
                            <Link to={this.state.linked + "/childrengrades"}>
                              <FontAwesomeIcon icon={faEdit} />
                              <span> Children grades</span>
                            </Link>
                          </li>    
                        ) : null }
                        {(this.state.isLoggedIn && this.state.userRole === "ROLE_STUDENT") ? (
                          <li>
                            <Link to={this.state.linked + "/studentgrades"}>
                              <FontAwesomeIcon icon={faEdit} />
                              <span> Grades</span>
                            </Link>
                          </li>                  
                        ) : null }
                        <WatchOutsideEvent onClickOutside={this.handleClickOutside} onMouseLeaveOutside={this.handleMouseLeaveOutsideSubmenu}>
                          {this.state.isLoggedIn ? (
                            <li>
                              <Link to={this.state.linked} onMouseEnter={() => this.toggleSubmenuOnHover()} >
                                <FontAwesomeIcon icon={faUser} />
                                <span> {this.state.user.firstName} {this.state.user.lastName} </span>
                                <FontAwesomeIcon icon={faChevronDown} size="xs" />
                              </Link>
                              <ul className={(this.state.isShowingSubmenu ? "showing" : null)} onMouseLeave={() => this.toggleSubmenuOnHover()} >
                                {(this.state.isLoggedIn && this.state.userRole === "ROLE_STUDENT") ? (
                                  <li>
                                    <Link to={this.state.linked + "/studentprofile"}>
                                      <FontAwesomeIcon icon={faColumns} />
                                      <span> Edit profile</span>
                                    </Link>
                                  </li>                  
                                ) : null }
                                {(this.state.isLoggedIn && this.state.userRole === "ROLE_PARENT") ? (
                                  <li>
                                    <Link to={this.state.linked + "/parentprofile"}>
                                      <FontAwesomeIcon icon={faColumns} />
                                      <span> Edit profile</span>
                                    </Link>
                                  </li>                  
                                ) : null }
                                {(this.state.isLoggedIn && this.state.userRole === "ROLE_TEACHER") ? (
                                  <li>
                                    <Link to={this.state.linked + "/teacherprofile"}>
                                      <FontAwesomeIcon icon={faColumns} />
                                      <span> Edit profile</span>
                                    </Link>
                                  </li>         
                                ) : null }
                                {(this.state.isLoggedIn && this.state.userRole === "ROLE_ADMIN") ? (
                                  <li>
                                    <Link to={this.state.linked + "/adminprofile"}>
                                      <FontAwesomeIcon icon={faColumns} />
                                      <span> Edit profile</span>
                                    </Link>
                                  </li>         
                                ) : null }
                                {(this.state.isLoggedIn && this.state.userRole === "ROLE_ADMIN") ? (
                                  <li>
                                    <div onClick={ () => this.handleGetLogFile() } >
                                      <FontAwesomeIcon icon={faFileArchive} />
                                      <span> Log file</span>
                                    </div>
                                  </li>         
                                ) : null }
                                <li>
                                  <a href="/" className="logout" onClick={this.handleLogOut}>
                                    <FontAwesomeIcon icon={faSignOutAlt} />
                                    <span> Logout</span>
                                  </a>
                                </li>
                              </ul>
                            </li>
                          ) : null }
                        </WatchOutsideEvent>
                      </ul>
                  </header>
                </WatchOutsideEvent>

                <div className="flex-wrapper">

                  <div className='phantom-header' />

                  
                      <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>}>
                    
                        {this.state.isLoggedIn ? (
                          <Redirect push to={{ pathname: this.state.linked }} />
                        ) : null }

                        <Switch>    

                          <Route
                            exact path="/" 
                            render={ (props) => <LoginPage {...props} 
                                                      username={this.state.username} 
                                                      password={this.state.password}
                                                      onChange={this.handleLoginChange}
                                                      onLogin={this.handleLogin} />} />
                          
                          <PrivateRoute
                            isLoggedIn={this.state.isLoggedIn}
                            isAuthorized={this.state.userRole === "ROLE_STUDENT"}
                            exact path="/students" 
                            render={(props) => <StudentsDashboard {...props}
                                                      username={this.state.username} 
                                                      password={this.state.password}
                                                      userRole={this.state.userRole}
                                                      user={this.state.user} 
                                                      onUserLogged={this.handleUpdateLoggedUser} />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_STUDENT"}
                                                exact path="/students/studentgrades" 
                                                render={(props) => <StudentGrades {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        user={this.state.user}
                                                                        onUserLogged={this.handleUpdateLoggedUser} />} />
                                              <PrivateRoute 
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_STUDENT"}
                                                exact path="/students/studentprofile" 
                                                render={(props) => <StudentProfil {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        onEditUser={this.handleUpdateLoggedUser} 
                                                                        linked={this.state.linked} />} />
                          
                          <PrivateRoute
                            isLoggedIn={this.state.isLoggedIn}
                            isAuthorized={this.state.userRole === "ROLE_TEACHER"}
                            exact path="/teachers" 
                            render={(props) => <TeacherDashboard {...props}
                                                      username={this.state.username} 
                                                      password={this.state.password}
                                                      userRole={this.state.userRole}
                                                      user={this.state.user} 
                                                      onUserLogged={this.handleUpdateLoggedUser} />} />
                                              <PrivateRoute 
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_TEACHER"}
                                                exact path="/teachers/teacherprofile" 
                                                render={(props) => <TeacherProfil {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        onEditUser={this.handleUpdateLoggedUser} />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_TEACHER"}
                                                exact path="/teachers/teachergrades" 
                                                render={(props) => <TeacherGrades {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        user={this.state.user}  />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_TEACHER"}
                                                exact path="/teachers/teachersubjects" 
                                                render={(props) => <TeacherSubjects {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        user={this.state.user}  />} />
                        
                          <PrivateRoute
                            isLoggedIn={this.state.isLoggedIn}
                            isAuthorized={this.state.userRole === "ROLE_PARENT"}
                            exact path="/parents" 
                            render={(props) => <ParentDashboard {...props}
                                                      username={this.state.username} 
                                                      password={this.state.password}
                                                      userRole={this.state.userRole}
                                                      user={this.state.user} 
                                                      onUserLogged={this.handleUpdateLoggedUser} />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_PARENT"}
                                                exact path="/parents/childrengrades" 
                                                render={(props) => <ParentGrades {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        user={this.state.user} />} />
                                              <PrivateRoute 
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_PARENT"}
                                                exact path="/parents/parentprofile" 
                                                render={(props) => <ParentProfil {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        onEditUser={this.handleUpdateLoggedUser} />} />
                          
                          <PrivateRoute
                            isLoggedIn={this.state.isLoggedIn}
                            isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                            exact path="/admins" 
                            render={(props) => <AdminDashboard {...props}
                                                      username={this.state.username} 
                                                      password={this.state.password}
                                                      userRole={this.state.userRole}
                                                      user={this.state.user}
                                                      onUserLogged={this.handleUpdateLoggedUser} />} />
                                              <PrivateRoute 
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/adminprofile" 
                                                render={(props) => <AdminProfil {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        onEditUser={this.handleUpdateLoggedUser} />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/adminsadministration" 
                                                render={(props) => <AdministrationAdmins {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        user={this.state.user}  />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/parentsadministration" 
                                                render={(props) => <AdministrationParents {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        user={this.state.user}  />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/subjectsadministration" 
                                                render={(props) => <AdministrationSubjects {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        user={this.state.user}  />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/classesadministration" 
                                                render={(props) => <AdministrationClasses {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        user={this.state.user}  />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/departmentsadministration" 
                                                render={(props) => <AdministrationDepartments {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        user={this.state.user}  />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/studentsadministration" 
                                                render={(props) => <AdministrationStudents {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        user={this.state.user}  />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/gradesadministration" 
                                                render={(props) => <AdministrationGrades {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        user={this.state.user}  />} />
                                              <PrivateRoute
                                                isLoggedIn={this.state.isLoggedIn}
                                                isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                                                exact path="/admins/teachersadministration" 
                                                render={(props) => <AdministrationTeachers {...props}
                                                                        username={this.state.username} 
                                                                        password={this.state.password}
                                                                        userRole={this.state.userRole}
                                                                        loggedUser={this.state.user}
                                                                        user={this.state.user}  />} />


                          <PrivateRoute 
                            isLoggedIn={this.state.isLoggedIn}
                            isAuthorized="true"
                            path="/policy" 
                            component={PrivacyPolicy} 
                          />
                          
                          <PrivateRoute 
                            isLoggedIn={this.state.isLoggedIn}
                            isAuthorized="true"
                            path="/terms" 
                            component={TermsAndConditions} 
                          />

                          <Route component={withRouter(NotFoundPage)} />     

                        </Switch>

                      </React.Suspense>
                    
                  <div className='phantom-footer' />

                  <footer className={ this.state.isLoggedIn ? "show showing" : "hide" } >

                  <div className='footer'>

                    <ul className={"nav " + (this.state.isShowingFaBarsMenu ? "showing" : null)} >
                      {/*<li>
                        <Link to={this.state.linked}>
                          <FontAwesomeIcon icon={faHome} />
                          <span> Home</span>
                        </Link>
                      </li>*/}
                      {!this.state.isLoggedIn ? (
                        <li>
                          <Link to={"/"}>
                            <FontAwesomeIcon icon={faSignInAlt} />
                            <span> Login</span>
                          </Link>
                        </li>
                      ) : null }



                      {this.state.isLoggedIn && this.state.userRole === "ROLE_TEACHER" ? (
                        <li>
                          <Link to={this.state.linked + "/teachersubjects"}>
                            <FontAwesomeIcon icon={faBookOpen} />
                              <span> Subjects </span>
                          </Link>
                          <ul>
                            <li>
                              <Link to={this.state.linked + "/teachergrades"}>
                                <FontAwesomeIcon icon={faEdit} />
                                <span> Grades</span>
                              </Link>
                            </li>                  
                          </ul>
                        </li>
                      ) : null }



                      {this.state.isLoggedIn && this.state.userRole === "ROLE_ADMIN" ? (
                        <li>
                          <Link to={this.state.linked + "/adminsadministration"}>
                            <FontAwesomeIcon icon={faTools} />
                            <span> Administration </span>
                          </Link>
                          <ul>
                            <li>
                              <Link to={this.state.linked + "/adminsadministration"}>
                                <FontAwesomeIcon icon={faUserCog} />
                                <span> Admins</span>
                              </Link>
                            </li>                  
                            <li>
                              <Link to={this.state.linked + "/teachersadministration"}>
                                <FontAwesomeIcon icon={faChalkboardTeacher} />
                                <span> Teachers</span>
                              </Link>
                            </li>                  
                            <li>
                              <Link to={this.state.linked + "/parentsadministration"}>
                                <FontAwesomeIcon icon={faUser} />
                                <span> Parents</span>
                              </Link>
                            </li>     
                            <li>
                              <Link to={this.state.linked + "/studentsadministration"}>
                                <FontAwesomeIcon icon={faUserGraduate} />
                                <span> Students</span>
                              </Link>
                            </li>         
                            <li>
                              <Link to={this.state.linked + "/classesadministration"}>
                                <FontAwesomeIcon icon={faChalkboard} />
                                <span> Classes</span>
                              </Link>
                            </li>   
                            <li>
                              <Link to={this.state.linked + "/departmentsadministration"}>
                                <FontAwesomeIcon icon={faUsers} />
                                <span> Departments</span>
                              </Link>
                            </li>         
                            <li>
                              <Link to={this.state.linked + "/subjectsadministration"}>
                                <FontAwesomeIcon icon={faBookOpen} />
                                <span> Subjects</span>
                              </Link>
                            </li>         
                            <li>
                              <Link to={this.state.linked + "/gradesadministration"}>
                                <FontAwesomeIcon icon={faEdit} />
                                <span> Grades</span>
                              </Link>
                            </li>               
                          </ul>
                        </li>
                      ) : null }
                      { (this.state.isLoggedIn && this.state.userRole === "ROLE_PARENT") ? (
                        <li>
                          <Link to={this.state.linked + "/childrengrades"}>
                            <FontAwesomeIcon icon={faEdit} />
                            <span> Children gades</span>
                          </Link>
                        </li>    
                        ) : null }
                      {(this.state.isLoggedIn && this.state.userRole === "ROLE_STUDENT") ? (
                        <li>
                          <Link to={this.state.linked + "/studentgrades"}>
                            <FontAwesomeIcon icon={faEdit} />
                            <span> Grades</span>
                          </Link>
                        </li>                  
                      ) : null }
                      {this.state.isLoggedIn ? (
                        <li>
                          <Link to={this.state.linked} >
                            <FontAwesomeIcon icon={faUser} />
                            <span> {this.state.user.firstName} {this.state.user.lastName} </span>
                          </Link>
                          <ul>
                            {(this.state.isLoggedIn && this.state.userRole === "ROLE_STUDENT") ? (
                              <li>
                                <Link to={this.state.linked + "/studentprofile"}>
                                  <FontAwesomeIcon icon={faColumns} />
                                  <span> Edit profile</span>
                                </Link>
                              </li>                  
                            ) : null }
                            {(this.state.isLoggedIn && this.state.userRole === "ROLE_PARENT") ? (
                              <li>
                                <Link to={this.state.linked + "/parentprofile"}>
                                  <FontAwesomeIcon icon={faColumns} />
                                  <span> Edit profile</span>
                                </Link>
                              </li>                  
                            ) : null }
                            {(this.state.isLoggedIn && this.state.userRole === "ROLE_TEACHER") ? (
                              <li>
                                <Link to={this.state.linked + "/teacherprofile"}>
                                  <FontAwesomeIcon icon={faColumns} />
                                  <span> Edit profile</span>
                                </Link>
                              </li>         
                            ) : null }
                            {(this.state.isLoggedIn && this.state.userRole === "ROLE_ADMIN") ? (
                              <li>
                                <Link to={this.state.linked + "/adminprofile"}>
                                  <FontAwesomeIcon icon={faColumns} />
                                  <span> Edit profile</span>
                                </Link>
                              </li>         
                            ) : null }
                            {(this.state.isLoggedIn && this.state.userRole === "ROLE_ADMIN") ? (
                              <li>
                                <div onClick={this.handleGetLogFile}>
                                  <FontAwesomeIcon icon={faFileArchive} />
                                  <span> Log file</span>
                                </div>
                              </li>         
                            ) : null }
                            <li>
                              <a href="/" className="logout" onClick={this.handleLogOut}>
                                <FontAwesomeIcon icon={faSignOutAlt} />
                                <span> Logout</span>
                              </a>
                            </li>
                          </ul>
                        </li>
                      ) : null }
                    </ul>

                    
                    <div className="footer-links">
                      <Link to="/policy">Privacy Policy</Link>
                      &nbsp; | &nbsp;
                      <Link to="/terms">Terms and conditions</Link>
                      &nbsp; | &nbsp;
                      <a href="https://github.com/drakulic-goran/project_backend">GitHub Source</a>
                    </div>
                    <div className="copyright">2019 - 2019 © - Goran Drakulić</div>

                    </div>
                  
                  </footer>

                </div>
          
              </>
            </BrowserRouter>            

          </CSSTransition>
        </TransitionGroup>
      </div>
      
      
      
    )}

}

export default App;