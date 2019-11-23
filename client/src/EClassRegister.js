import React, {Component, lazy} from 'react';
import {BrowserRouter, Switch, Route, Redirect, withRouter } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars  } from '@fortawesome/free-solid-svg-icons';
import './App.css';
import PrivateRoute from './components/Common/PrivateRoute';
import LoginPage from './components/LoginForm/LoginForm';
import NotFoundPage from './components/Common/NotFoundPage';
//import StudentsDashboard from './components/StudentDashboard/StudentsDashboard';
//import TeacherDashboard from './components/TeacherDashboard/TeacherDashboard';
//import ParentDashboard from './components/ParentDashboard/ParentDashboard';
//import AdminDashboard from './components/AdminDashboard/AdminDashboard';

const StudentsDashboard = lazy(() => import('./components/StudentDashboard/StudentsDashboard'));
const TeacherDashboard = lazy(() => import('./components/TeacherDashboard/TeacherDashboard'));
const ParentDashboard = lazy(() => import('./components/ParentDashboard/ParentDashboard'));
const AdminDashboard = lazy(() => import('./components/AdminDashboard/AdminDashboard'));


class EClassRegister extends Component {

  constructor(props) {
    super(props)
    this.state = {
      username: '',
      password: '',
      user: {},
      userRole: '',
      linked: '/',
      isLoggedIn: false,
      isLoading: false, 
      isError: false
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
      },)
    if( response.ok ) {
        const userAccount = await response.json();
        //console.log(userAccount);
        const user = {...userAccount.user};
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
    } else if( response.status === 401 ) {
        alert("Wrong username or password!");
        this.setState({ isLoading: false, username: '', password: '' })
    } else {
        this.setState({ isLoading: false, isError: true })
    }
  }

  handleLogOut = (e) => {
    this.setState({isLoggedIn: false, user: {}, userRole: '', username: '', password: '', linked:"/" })
    e.preventDefault()
  }


  render () {

    
    return (
      
      <div className="App Site">
        <div className="Site-content">
          <div className="App-header">
            <FontAwesomeIcon icon={faBars } />
            <div>
              <nav>
              Home
              Users
              Contact

              <span>{this.state.isLoggedIn ? (
              <button className="logout-btn" onClick={this.handleLogOut}>LogOut</button>
              ) : null }</span>
              </nav>
            </div>
          </div>
          <div className='phantom-header' />

          <BrowserRouter>
            <>
              <React.Suspense fallback={<h1>Loading...</h1>}>
            
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
                    path="/students" 
                    render={(props) => <StudentsDashboard {...props}
                                              username={this.state.username} 
                                              password={this.state.password}
                                              userRole={this.state.userRole}
                                              user={this.state.user} />} />
                  <PrivateRoute
                    isLoggedIn={this.state.isLoggedIn}
                    isAuthorized={this.state.userRole === "ROLE_TEACHER"}
                    path="/teachers" 
                    render={(props) => <TeacherDashboard {...props}
                                              username={this.state.username} 
                                              password={this.state.password}
                                              userRole={this.state.userRole}
                                              user={this.state.user} />} />
                  <PrivateRoute
                    isLoggedIn={this.state.isLoggedIn}
                    isAuthorized={this.state.userRole === "ROLE_PARENT"}
                    path="/parents" 
                    render={(props) => <ParentDashboard {...props}
                                              username={this.state.username} 
                                              password={this.state.password}
                                              userRole={this.state.userRole}
                                              user={this.state.user} />} />
                  <PrivateRoute
                    isLoggedIn={this.state.isLoggedIn}
                    isAuthorized={this.state.userRole === "ROLE_ADMIN"}
                    path="/admins" 
                    render={(props) => <AdminDashboard {...props}
                                              username={this.state.username} 
                                              password={this.state.password}
                                              userRole={this.state.userRole}
                                              user={this.state.user} />} />
                  <Route component={withRouter(NotFoundPage)} />                
                </Switch>
              </React.Suspense>
            </>
          </BrowserRouter>            
        </div>
        
        <div className='phantom-footer' />
        <footer id='App-footer' className='App-footer'>
              © 2019. - 2019. <a href='https://github.com/drakulic-goran/project_backend'>GitHub - Goran Drakulić</a>
        </footer>

      </div>
      

      
    )}
}

export default EClassRegister;