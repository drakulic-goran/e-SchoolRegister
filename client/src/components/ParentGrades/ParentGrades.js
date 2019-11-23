import React, { Component } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/StudentGrades.css'


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

class StudentGrades extends Component {
    constructor(props) {
        super(props)
        this.state = {
            grades: [],
            isLoading: false,
            isError: false,
            isLoggedIn: false,
            activeStudentView: null, 
            activeSemesterView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        if(this.props) {
            const { username, password } = this.props
            const response = await fetch('http://localhost:8080/project/grades/parent/' /*+ '?useLegacyDatetimeCode=false'*/, {
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
            if( response.ok ) {
                const grades = await response.json();
                this.setState({ grades, isLoading: false })
                //console.log(this.state.grades)
            } else {
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
                this.setState({ isLoading: false, isError: true })
            }

            const response1 = await fetch('http://localhost:8080/project/subjects/parent/' /*+ '?useLegacyDatetimeCode=false'*/, {
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
                if( response1.ok ) {
                    const subjects = await response1.json();
                    const updateSubjects = []
                    //console.log(subjects)
                    for (let i = 0; i<subjects.length; i++) {
                        updateSubjects.push({student: subjects[i].student, clas: subjects[i].teacher_subject_department.teachingClass, department: subjects[i].teacher_subject_department.teachingDepartment, grade: {semester: "FIRST_MIDTERM", teacher_subject_department: subjects[i].teacher_subject_department}, subject: subjects[i].teacher_subject_department.teachingSubject});
                    }
                    for (let i = 0; i<subjects.length; i++) {
                        updateSubjects.push({student: subjects[i].student, clas: subjects[i].teacher_subject_department.teachingClass, department: subjects[i].teacher_subject_department.teachingDepartment, grade: {semester: "SECOND_MIDTERM", teacher_subject_department: subjects[i].teacher_subject_department}, subject: subjects[i].teacher_subject_department.teachingSubject});
                    }
                    //updateSubjects.sort((a, b) => a.subject.subjectName.localeCompare(b.subject.subjectName))
                    //console.log(updateSubjects)
                    this.setState({ grades: this.state.grades.concat(updateSubjects), isLoading: false })
                    //console.log(this.state.grades)
                } else {
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
                    this.setState({ isLoading: false, isError: true })
                }

        }
    }

    shouldComponentUpdate(nextProps, nextState) {
        if (this.state !== nextState) {
            return true;
        }
        return false;
    }

    /*renderTableData() {
        return this.state.grades.map((grade) => { 
                return (
                    <tr key={grade.id}>
                        <td>{grade.student.firstName}</td>
                        <td>{grade.student.lastName}</td>
                        <td>{grade.id}</td>
                        <td>{grade.gradeValue}</td>
                        <td>{grade.gradeMadeDate}</td>
                        <td>{grade.semester}</td>
                        <td>{grade.teacher_subject_department.schoolYear}</td>
                        <td>{grade.teacher_subject_department.teachingSubject.subjectName}</td>
                        <td>{grade.teacher_subject_department.teachingTeacher.firstName}</td>
                        <td>{grade.teacher_subject_department.teachingTeacher.lastName}</td>
                    </tr>
                )
            })
     }

    renderDivData() {
        const grouppedGrades = _.groupByMulti(this.state.grades, [ function(currentObject) { return currentObject.student.lastName }, 
                                                                    function(currentObject) { return currentObject.student.firstName }, 
                                                                    function(currentObject) { return currentObject.teacher_subject_department.teachingSubject.subjectName } ] )    
        return Object.keys(grouppedGrades).map(studentLastName => (
            <ul key={studentLastName}>{studentLastName} 
                {Object.keys(grouppedGrades[studentLastName]).map(student => 
                <div key={student}> {student}: {Object.keys(grouppedGrades[studentLastName][student]).map(subject => 
                    <li key={subject}>{subject}: {_.join(grouppedGrades[studentLastName][student][subject].map(grade => grade.gradeValue), ', ')}</li>)} 
                </div>)}
            </ul>
        ))*/

        /*return Object.keys(grouppedGrades).map(student => (
            <div>
                <ul key={student}>{grouppedGrades[student][0].student.firstName}: 
                    {Object.keys(grouppedGrades[student]).map(subject => 
                    <li key={subject}>{subject}: {_.join(grouppedGrades[student][subject].map(grade => grade.gradeValue), ', ')}</li>)}
                </ul>
            </div>
            ))*/

    /*}

    renderSemesterData() {
        const grouppedGrades = _.groupByMulti(this.state.grades, [ function(currentObject) { return currentObject.student.lastName }, 
                                                                    function(currentObject) { return currentObject.student.firstName }, 
                                                                    function(currentObject) { return currentObject.semester }, 
                                                                    function(currentObject) { return currentObject.teacher_subject_department.teachingSubject.subjectName } ] )    
        return Object.keys(grouppedGrades).map(studentLastName => (
            <ul key={studentLastName}>{studentLastName} 
                {Object.keys(grouppedGrades[studentLastName]).map(student => 
                <div key={student}> {student}: {Object.keys(grouppedGrades[studentLastName][student]).map(semester => 
                    <ul key={semester}>{semester}: {Object.keys(grouppedGrades[studentLastName][student][semester]).map(subject => 
                        <li key={subject}>{subject}: {_.join(grouppedGrades[studentLastName][student][semester][subject].map(grade => grade.gradeValue), ', ')}</li>)} 
                    </ul>)}
                </div>)}
            </ul>
        ))
    }*/

    handleSemesterClick = (id) => {
        this.state.activeSemesterView !== id ?
            this.setState({activeSemesterView: id})
        : this.setState({activeSemesterView: null})
        //console.log(this.state.activeSemester)
    }

    handleClassClick = (id) => {
        this.state.activeClassView !== id ?
            this.setState({activeClassView: id, activeSemesterView: null})
        : this.setState({activeClassView: null, activeSemesterView: null})
    }

    handleStudentClick = (id) => {
        this.state.activeStudentView !== id ?
            this.setState({activeStudentView: id, activeClassView: null, activeSemesterView: null})
        : this.setState({activeStudentView: null, activeClassView: null, activeSemesterView: null})
        //console.log(this.state.activeSchoolYear)
    }

    handleSchoolYearClick = (id) => {
        this.state.activeSchoolYear !== id ?
            this.setState({activeSchoolYear: id, activeClassView: null, activeStudentView: null, activeSemesterView: null})
        : this.setState({activeSchoolYear: null, activeClassView: null, activeStudentView: null, activeSemesterView: null})
        //console.log(this.state.activeSchoolYear)
    }


    handleGradesByStudentAndSemesterAndSubject() {
        //console.log(this.state.grades)
        const grouppedGrades = _.groupByMulti(this.state.grades, [ 
            function(currentObject) { return currentObject.grade.teacher_subject_department.schoolYear }, 
            function(currentObject) { return currentObject.student.lastName + " " + currentObject.student.firstName }, 
            function(currentObject) { return ( currentObject.grade.teacher_subject_department.teachingClass.classLabel + "-" + currentObject.grade.teacher_subject_department.teachingDepartment.departmentLabel ) }, 
            function(currentObject) { return currentObject.grade.semester }, 
            function(currentObject) { return currentObject.subject.subjectName } ] ) 
        
        //console.log(grouppedGrades)

        return Object.keys(grouppedGrades).map((year, id5) => (
            <div className="student-grades-school-year" key={year}><h3>School year: {year}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSchoolYear !== id5 ? faEye : faEyeSlash} onClick={() => this.handleSchoolYearClick(id5)} /></h3> 
                {Object.keys(grouppedGrades[year]).map((student, id) => 
                <div className={this.state.activeSchoolYear === id5 ? "student-grades-student" : "hide"} key={student}><h3>{student} <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeStudentView !== id ? faEye : faEyeSlash} onClick={() => this.handleStudentClick(id)} /></h3> 
                    {Object.keys(grouppedGrades[year][student]).map((classDepartment, id1) => 
                    <div className={this.state.activeStudentView === id ? "student-grades-midterm" : "hide"} key={classDepartment}><h3>{classDepartment} <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeClassView !== id1 ? faEye : faEyeSlash} onClick={() => this.handleClassClick(id1)} /></h3> 
                        {Object.keys(grouppedGrades[year][student][classDepartment]).map((semester, id4) => 
                        <div className={this.state.activeClassView === id1 ? "student-grades-midterm-in-class" : "hide"} key={semester}><h3>{semester} <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSemesterView !== id4 ? faEye : faEyeSlash} onClick={() => this.handleSemesterClick(id4)} /></h3> 
                            {Object.keys(grouppedGrades[year][student][classDepartment][semester]).map(subject => 
                            <div className={this.state.activeSemesterView === id4 ? "student-grades-subject" : "hide"} key={subject}>{subject}: 
                                {grouppedGrades[year][student][classDepartment][semester][subject].map(grade => grade.grade.gradeValue ?
                                <div className="student-grades-grade" key={grade.grade.id}>{grade.grade.gradeValue}</div> : null)}
                            </div>)} 
                        </div>)}
                    </div>)}
                </div>)}
            </div>
        ))
    }


    render() {

        const {grades } = this.state

        /*if( isError ) {
            return <div>Error!</div>
        }*/

        return grades 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>


                   {/* <h1 id='title'>GRADES</h1>
                    <table id="grades">
                        <tbody>
                            {this.renderTableData()}
                        </tbody>
                    </table>
                    --------------------------------
                    <div>
                        {this.renderDivData()}
                    </div>
                    --------------------------------
                    <div>
                        {this.renderSemesterData()}
                    </div>
                    --------------------------------*/} 
                    <div>
                        {this.handleGradesByStudentAndSemesterAndSubject()}
                    </div>

                </React.Suspense>

            </div>
        )
        : null
    }
}

export default StudentGrades