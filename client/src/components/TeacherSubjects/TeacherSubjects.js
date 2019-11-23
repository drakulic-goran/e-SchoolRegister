import React, { Component } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/TeacherGrades.css'


let _ = require('lodash');

_.groupByMulti = function (obj, values, context) {
    if (!values.length)
        return obj;
    var byFirst = _.groupBy(obj, values[0], context),
        rest = values.slice(1);
    for (var prop in byFirst) {
        byFirst[prop] = _.groupByMulti(byFirst[prop], rest, context);
    }
    return byFirst;
};

class TeacherSubjects extends Component {
    constructor(props) {
        super(props)
        this.state = {
            subjects: [],
            subDepartments: [],
            isLoading: false,
            isError: false,
            isLoggedIn: false,
            activeSubjectClassesView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        if(this.props) {
            const { username, password } = this.props
            const response = await fetch('http://localhost:8080/project/subjects/teacher/' /*+ '?useLegacyDatetimeCode=false'*/, {
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
                const subjects = await response.json();
                this.setState({ subjects })
                if(subjects.length === 0) {
                    alert("No assigned subject/s!")
                }
                //console.log(this.state.subjects) 
                //console.log(response) 
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
                this.setState({ isLoading: false, isError: true })
            }

            if(!this.state.isError) {
                const response1 = await fetch('http://localhost:8080/project/subjects/teacher/departments/' /*+ '?useLegacyDatetimeCode=false'*/, {
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
                if( !this.state.isCatchError && response1.ok ) {
                    const subDepartments = await response1.json();
                    this.setState({ subDepartments, isLoading: false })
                    //console.log(this.state.subDepartments) 
                    //console.log(response1) 
                } else if(!this.state.isCatchError) {
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
                    this.setState({ isLoading: false, isError: true })
                }
            }
        } else {
            this.setState({ loggedUser: null, isLoggedIn:false, isLoading: false })
        }

    }

    shouldComponentUpdate(nextProps, nextState) {
        if (this.state !== nextState) {
            return true;
        }
        return false;
      }

    
    renderSubjectData() {
        return this.state.subjects.map((subject, id) => (
            <div className={this.state.activeSubjectClassesView === 0 ? "teacher-subjects-subjects" : "hide"} key={subject.id}><h3>{subject.subjectName}</h3>
                {subject.classes.map((classe, id1) =>
                //<div className="class-info" key={id1}><h5>{classe.learningProgram}</h5> 
                <span key={id1}>
                    <div className="teacher-subjects-classes" key={classe.clas.id}> {classe.clas.classLabel}
                        {/*{classe.clas.departments.map((department, id2) => 
                        <span className="grades-info" key={id2} > {classe.clas.classLabel}-{department.department.departmentLabel}
                        </span>)}*/}
                    </div>
                </span>
                //</div>
                )}
            </div>
        ))
    }

    renderSubjectDepartmentsData() {
        const grouppedSubjects = _.groupByMulti(this.state.subDepartments, [ 
            function(currentObject) { return currentObject.subject.id },
            function(currentObject) { return currentObject.subject.subjectName }, ] )    
        //console.log(grouppedSubjects)
        return Object.keys(grouppedSubjects).map(subjectId => (
            <span className={this.state.activeSubjectClassesView === 1 ? "" : "hide"} key={subjectId}>
                {Object.keys(grouppedSubjects[subjectId]).map(subjectName => 
                <div className="teacher-subjects-subjects" key={subjectName}><h3>{subjectName}</h3>
                    {grouppedSubjects[subjectId][subjectName].map((subjectGroup, id) => (
                    <span key={id}>
                        {/*{subjectGroup.subject.classes.map((classe, id1) =>
                        //<div className="class-info" key={id1}><h5>{classe.learningProgram}</h5> 
                        <span key={id1}>
                            <div className="subject" key={subjectGroup.teacher_subject_department.teachingClass.id}> {subjectGroup.teacher_subject_department.teachingClass.classLabel}  {(subjectGroup.teacher_subject_department.teachingClass.classLabel) ? */}
                                <span className="teacher-subjects-classes" key={subjectGroup.teacher_subject_department.teachingDepartment.id} > {subjectGroup.teacher_subject_department.teachingClass.classLabel}-{subjectGroup.teacher_subject_department.teachingDepartment.departmentLabel}
                                </span> {/*: null}
                            </div>
                        </span>
                        //</div>
                        })}*/}
                    </span>))}
                </div>)}
            </span>
        ))
    }

    handleSubjectClassesClick = (id) => {
        this.state.activeSubjectClassesView !== id ?
            this.setState({activeSubjectClassesView: id})
        : this.setState({activeSubjectClassesView: null})
    }


    render() {

        const { subjects, subDepartments } = this.state

        /*if( isError ) {
            return <div>Error!</div>
        }*/

        return subjects.length > 0 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    <div className="teacher-subjects-header">
                        <h3>Assigned subject/s with posible classes  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSubjectClassesView !== 0 ? faEye : faEyeSlash} onClick={() => this.handleSubjectClassesClick(0)} /></h3>
                        {this.renderSubjectData()}
                    </div>

                    <br/>

                    {subDepartments.length > 0 
                    ? (
                        <div className="teacher-subjects-header">
                            <h3>Assigned department/s  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSubjectClassesView !== 1 ? faEye : faEyeSlash} onClick={() => this.handleSubjectClassesClick(1)} /></h3>
                            {this.renderSubjectDepartmentsData()}
                        </div>
                    ) : (
                        <div className="teacher-subjects-header">
                            <h1>No assigned department/s!</h1>
                        </div>
                    )}
                    
                </React.Suspense>

            </div>
        )
        : null

    }
}

export default TeacherSubjects