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
            activeSchoolYear: null,
            activeSemester: null,
            activeClassView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        if(this.props) {
            const { username, password } = this.props
            const response = await fetch('http://localhost:8080/project/grades/student/' /*+ '?useLegacyDatetimeCode=false'*/, {
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
                //alert("Error: " + err.message)        
                this.setState({ isLoading: false, isError: true })
            }

            const response1 = await fetch('http://localhost:8080/project/subjects/student/' /*+ '?useLegacyDatetimeCode=false'*/, {
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
                    console.log(this.props.user)
                    for (let i = 0; i<subjects.length; i++) {
                        updateSubjects.push({clas: subjects[i].teachingClass, department: subjects[i].teachingDepartment, grade: {semester: "FIRST_MIDTERM", teacher_subject_department: subjects[i]}, subject: subjects[i].teachingSubject});
                    }
                    for (let i = 0; i<subjects.length; i++) {
                        updateSubjects.push({clas: subjects[i].teachingClass, department: subjects[i].teachingDepartment, grade: {semester: "SECOND_MIDTERM", teacher_subject_department: subjects[i]}, subject: subjects[i].teachingSubject});
                    }
                    //console.log(updateSubjects)
                    this.setState({ grades: this.state.grades.concat(updateSubjects), isLoading: false })
                    //console.log(this.state.grades)
                } else {
                    const err = await response1.json();
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

   /* renderTableData() {
        return this.state.grades.map((grade) => { 
                return (
                    <tr key={grade.id}>
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

    renderListData() {
    const grouppedGrades = _.groupBy(this.state.grades, function(currentObject) { return currentObject.teacher_subject_department.teachingSubject.subjectName } )
    const grades = Object.values(grouppedGrades)
    return grades.map((subject) => { 
        return subject.map((grade) => {
            return (
                <li key={grade.id}>
                    {grade.gradeValue}, {grade.teacher_subject_department.teachingSubject.subjectName}
                </li>
            )
        })
    })
    }

    renderListGroupedGrades() {
        const grouppedGrades = _.groupBy(this.state.grades, function(currentObject) { return currentObject.teacher_subject_department.teachingSubject.subjectName } )
        return Object.keys(grouppedGrades).map(key => (
                <ul key={key}>{key}: {grouppedGrades[key].map((d) => <li key={d.id}>{d.gradeValue}</li>)}</ul>
          ))
    }

    renderGroupedGrades() {
        const grouppedGrades = _.groupBy(this.state.grades, function(currentObject) { return currentObject.teacher_subject_department.teachingSubject.subjectName } )
        return Object.keys(grouppedGrades).map(key => (
                <ul key={key}>{key}: {_.join(grouppedGrades[key].map(u => u.gradeValue), ', ')}</ul>
          ))
    }

    renderSemesterData() {
        const grouppedGrades = _.groupByMulti(this.state.grades, [ function(currentObject) { return currentObject.semester }, function(currentObject) { return currentObject.teacher_subject_department.teachingSubject.subjectName } ] )    
        return Object.keys(grouppedGrades).map(key => (
                <ul key={key}>{key}: 
                    {Object.keys(grouppedGrades[key]).map(subject => 
                    <li key={subject}>{subject}: {_.join(grouppedGrades[key][subject].map(grade => grade.gradeValue), ', ')}</li>)}
                </ul>
        ))
    }*/

    // addActiveClass = (index) => {
    //     const activeSchoolYear = [...this.state.activeSchoolYear.slice(0, index), 
    //             !this.state.activeSchoolYear[index], 
    //             this.state.activeSchoolYear.slice(index + 1)];
    //     this.setState({activeSchoolYear});
    //   }
    
    handleSemesterClick = (id) => {
        this.state.activeSemester !== id ?
            this.setState({activeSemester: id})
        : this.setState({activeSemester: null})
        //console.log(this.state.activeSemester)
    }

    handleClassClick = (id) => {
        this.state.activeClassView !== id ?
            this.setState({activeClassView: id, activeSemester: null})
        : this.setState({activeClassView: null, activeSemester: null})
    }

    handleSchoolYearClick = (id) => {
        this.state.activeSchoolYear !== id ?
            this.setState({activeSchoolYear: id, activeClassView: null, activeSemester: null})
        : this.setState({activeSchoolYear: null, activeClassView: null, activeSemester: null})
        //console.log(this.state.activeSchoolYear)
    }

    renderGradesBySemesterAndSubject() {
        //console.log(this.state.grades)
        const grouppedGrades = _.groupByMulti(this.state.grades, [ 
            function(currentObject) { return currentObject.grade.teacher_subject_department.schoolYear }, 
            function(currentObject) { return (currentObject.grade.teacher_subject_department.teachingClass.classLabel + "-" + currentObject.grade.teacher_subject_department.teachingDepartment.departmentLabel) }, 
            function(currentObject) { return currentObject.grade.semester }, 
            function(currentObject) { return currentObject.subject.subjectName } ] ) 
        //console.log(grouppedGrades)
        // let showingYear = grouppedGrades.length>0 ? grouppedGrades.map((element) => false) : []
        // console.log(showingYear)
        //let showingYear = [false, false, false]
        //grouppedGrades ? grouppedGrades.forEach(() => showingYear = [...showingYear, false] ) : showingYear = [...showingYear]
        //this.setState({activeSchoolYear: showingYear });
        //const activeClasses = this.activeClasses.slice();
        //className={ activeClasses[id] ? "grades-header show showing" : "hide" } onClick={() => this.addActiveClass(id)} 
        return Object.keys(grouppedGrades).map((year, id) => (
            <div className="student-grades-school-year" key={year}><h3>School year: {year}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSchoolYear !== id ? faEye : faEyeSlash} onClick={() => this.handleSchoolYearClick(id)} /></h3> 
                {Object.keys(grouppedGrades[year]).map((classDepartment, id2) => 
                <div className={this.state.activeSchoolYear === id ? "student-grades-class" : "hide"} key={classDepartment}><h3>{classDepartment}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeClassView !== id2 ? faEye : faEyeSlash} onClick={() => this.handleClassClick(id2)} /></h3> 
                    {Object.keys(grouppedGrades[year][classDepartment]).map((semester, id1) => 
                    <div className={this.state.activeClassView === id2 ? "student-grades-midterm" : "hide"} key={semester}><h3>{semester}:  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSemester !== id1 ? faEye : faEyeSlash} onClick={() => this.handleSemesterClick(id1)} /></h3>
                        {Object.keys(grouppedGrades[year][classDepartment][semester]).map(subject => 
                        <div className={this.state.activeSemester === id1 ? "student-grades-subject cursor-none" : "hide"} key={subject}>{subject}: 
                            {grouppedGrades[year][classDepartment][semester][subject].map(grade => grade.grade.gradeValue ?
                            <div className="student-grades-grade cursor-none" key={grade.grade.id}>{grade.grade.gradeValue}</div> : null)}
                        </div>)}
                    </div>)}
                </div>)}
            </div>))
    }


    render() {
        const { grades } = this.state

        /*if( isError ) {
            return <div>Error!</div>
        }*/

        return grades 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    {/*<h1 id='title'>GRADES</h1>
                    <table id="grades">
                        <tbody>
                            {this.renderTableData()}
                        </tbody>
                    </table>
                    <ul>
                        {this.renderListData()}
                    </ul>
                    <div>
                        {this.renderListGroupedGrades()}
                    </div>
                    <div>
                        {this.renderGroupedGrades()}
                    </div>
                    <div>
                        {this.renderSemesterData()}
        </div>*/}
                    <div>
                        {this.renderGradesBySemesterAndSubject()}
                    </div>

                </React.Suspense>

            </div>
        )
        : null
    }
}

export default StudentGrades