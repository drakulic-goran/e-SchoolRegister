import React, { Component } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faPlusCircle, faMinus, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import StudentGradesForm from '../StudentGradesForm/StudentGradesForm'
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

class TeacherGrades extends Component {
    constructor(props) {
        super(props)
        this.state = {
            loggedUser: this.props.user,
            grades: [],
            gradesAlone: [],
            isLoading: false,
            isError: false,
            isLoggedIn: false,
            selectedGrade: null,
            newGrade: null,
            errMsg: null,
            activeSchoolYearView: null, 
            activeSemesterView: null, 
            activeClassView: null, 
            activeSubjectView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        if(this.props) {
            const { username, password } = this.props
            const response = await fetch('http://localhost:8080/project/grades/teacher/' /*+ '?useLegacyDatetimeCode=false'*/, {
            mode: 'cors',
            method: 'GET',
            headers: {
                'Authorization': 'Basic ' + window.btoa(username + ":" + password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },).catch((error) => {
                this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
                alert(error.toString().substr(4) + "!")
            })
            if( !this.state.isCatchError && response.ok ) {
                const grades = await response.json()
                this.setState({ gradesAlone: grades, isLoading: false })
                /*if(grades.length === 0) {
                    alert("No grades!")
                }*/
                //console.log(this.state.gradesAlone)
            } else if (!this.state.isCatchError) {
                //alert(response.statusText)
                //alert(response.console.error())
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
                this.setState({ gradesAlone: [], isLoading: false, isError: true })
            }

            const response1 = await fetch('http://localhost:8080/project/subjects/teacher/students/' /*+ '?useLegacyDatetimeCode=false'*/, {
            mode: 'cors',
            method: 'GET',
            headers: {
                'Authorization': 'Basic ' + window.btoa(username + ":" + password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },).catch((error) => {
                this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
                alert(error.toString().substr(4) + "!")
            })
            if( !this.state.isCatchError && response1.ok ) {
                const subjects = await response1.json();
                const updateSubjects = []
                //console.log(subjects)
                for (let i = 0; i<subjects.length; i++) {
                    updateSubjects.push({student: subjects[i].student,  semester: "FIRST_MIDTERM", teacher_subject_department: subjects[i].teacher_subject_department });
                }
                for (let i = 0; i<subjects.length; i++) {
                    updateSubjects.push({student: subjects[i].student,  semester: "SECOND_MIDTERM", teacher_subject_department: subjects[i].teacher_subject_department });
                }
                //console.log(updateSubjects)
                this.setState({ grades: this.state.gradesAlone.concat(updateSubjects), isLoading: false })
                //console.log(this.state.grades)
            } else if (!this.state.isCatchError) {
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
                this.setState({ grades: [], isLoading: false, isError: true })
            }

        }
    }

    shouldComponentUpdate(nextProps, nextState) {
        if (this.state !== nextState) {
            return true;
        }
        /*if (this.state.grades !== nextState.grades) {
          return true;
        }
        if (this.state.selectedGrade !== nextState.selectedGrade) {
          return true;
        }
        if (this.state.newGrade !== nextState.newGrade) {
            return true;
        }  */
        return false;
      }

    handleEdit = (gradeId, subjectName) => { const grade = this.state.grades.find( gr => gr.id === gradeId ); this.setState({ selectedGrade: {...grade, subjectName} }); }
    handleNew = (studentId, subjectId, subjectName, semester) => { this.setState({ newGrade: {student: studentId, subject: subjectId, subjectName: subjectName, semester: semester, gradeValue: null}}); }


    handleSubjectClick = (id) => {
        this.state.activeSubjectView !== id ?
            this.setState({activeSubjectView: id})
        : this.setState({activeSubjectView: null})
    }

    handleClassClick = (id) => {
        this.state.activeClassView !== id ?
            this.setState({activeClassView: id, activeSubjectView: null})
        : this.setState({activeClassView: null, activeSubjectView: null})
    }

    handleSemesterClick = (id) => {
        this.state.activeSemesterView !== id ?
            this.setState({activeSemesterView: id, activeClassView: null, activeSubjectView: null})
        : this.setState({activeSemesterView: null, activeClassView: null, activeSubjectView: null})
    }

    handleSchoolYearClick = (id) => {
        this.state.activeSchoolYearView !== id ?
            this.setState({activeSchoolYearView: id, activeSemesterView: null, activeClassView: null, activeSubjectView: null})
        : this.setState({activeSchoolYearView: null, activeSemesterView: null, activeClassView: null, activeSubjectView: null})
    }

    renderGradesByClDepStudSemSubj() {
        const grouppedGrades = _.groupByMulti(this.state.grades, [ 
            function(currentObject) { return currentObject.teacher_subject_department.schoolYear }, 
            function(currentObject) { return currentObject.semester }, 
            function(currentObject) { return (currentObject.teacher_subject_department.teachingClass.classLabel + "-" + currentObject.teacher_subject_department.teachingDepartment.departmentLabel) }, 
            function(currentObject) { return currentObject.teacher_subject_department.teachingSubject.id }, 
            function(currentObject) { return currentObject.teacher_subject_department.teachingSubject.subjectName }, 
            function(currentObject) { return currentObject.student.id }, 
            function(currentObject) { return (currentObject.student.lastName + " " + currentObject.student.firstName) },] )    
        //console.log(grouppedGrades)
        return Object.keys(grouppedGrades).map((year, id) => (
            <div className="teacher-grades-school-year" key={year}><h3>{year}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSchoolYearView !== id ? faEye : faEyeSlash} onClick={() => this.handleSchoolYearClick(id)} /></h3>
                {Object.keys(grouppedGrades[year]).map((semester, id1) => 
                <div className={this.state.activeSchoolYearView === id ? "teacher-grades-semester" : "hide"} key={semester}><h3>{semester}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSemesterView !== id1 ? faEye : faEyeSlash} onClick={() => this.handleSemesterClick(id1)} /></h3>
                    {Object.keys(grouppedGrades[year][semester]).map((classDepartment, id2) => 
                    <div className={this.state.activeSemesterView === id1 ? "teacher-grades-class-department" : "hide"} key={classDepartment}>{classDepartment} <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeClassView !== id2 ? faEye : faEyeSlash} onClick={() => this.handleClassClick(id2)} />
                        {Object.keys(grouppedGrades[year][semester][classDepartment]).map((subjectId, id3) => 
                        <span className={this.state.activeClassView === id2 ? "" : "hide"} key={subjectId}>
                            {Object.keys(grouppedGrades[year][semester][classDepartment][subjectId]).map((subjectName) => 
                            <div className="teacher-grades-subject" key={subjectName}><h4>{subjectName}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSubjectView !== id3 ? faEye : faEyeSlash} onClick={() => this.handleSubjectClick(id3)} /></h4> 
                                {Object.keys(grouppedGrades[year][semester][classDepartment][subjectId][subjectName]).map(studentId => 
                                <span className={this.state.activeSubjectView === id3 ? "" : "hide"} key={studentId}>
                                    {Object.keys(grouppedGrades[year][semester][classDepartment][subjectId][subjectName][studentId]).map(student => 
                                    <div className="teacher-grades-student" key={student}> {student}:
                                        {grouppedGrades[year][semester][classDepartment][subjectId][subjectName][studentId][student].map(grade => grade.gradeValue ?
                                            <span key={grade.id}>
                                                {grade.teacher_subject_department.status === 1 && grade.student.status === 1 ? 
                                                    <div className="teacher-grades-grades cursor-pointer" onClick={ () => this.handleEdit(grade.id, subjectName) }>{grade.gradeValue}</div> 
                                                    :
                                                    <div className="teacher-grades-grades">{grade.gradeValue}</div> }
                                                {grade.teacher_subject_department.status === 1 && grade.student.status === 1 ? 
                                                    <FontAwesomeIcon className="cursor-pointer icon-delete" icon={faMinus} onClick={ () => this.handleDelete(grade.id) } /> 
                                                : null}
                                            </span> 
                                        : null)}
                                        {grouppedGrades[year][semester][classDepartment][subjectId][subjectName][studentId][student].length > 0 ? 
                                            grouppedGrades[year][semester][classDepartment][subjectId][subjectName][studentId][student].filter((tsd) => tsd.teacher_subject_department.status === 1).length>0
                                            /*grouppedGrades[year][semester][classDepartment][subjectId][subjectName][studentId][student][0].teacher_subject_department.status === 1 */ 
                                            && 
                                            grouppedGrades[year][semester][classDepartment][subjectId][subjectName][studentId][student].filter((tsd) => tsd.student.status === 1).length>0
                                            /*grouppedGrades[year][semester][classDepartment][subjectId][subjectName][studentId][student][0].student.status === 1*/ ?
                                                <FontAwesomeIcon className="cursor-pointer icon-size" icon={faPlusCircle} onClick={ () => this.handleNew(studentId, subjectId, subjectName, semester) } />
                                            : null
                                        : null }
                                    </div>)}
                                </span>)} 
                            </div>)}
                        </span>)}
                    </div>)}
                </div>)}
            </div>
        ))
    }

    handleEditSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/grades/change/'+ this.state.selectedGrade.id +'/value/'+Number(this.state.selectedGrade.gradeValue), {
            mode: 'cors',
            method: 'PUT',
            headers: {
                'Authorization': 'Basic ' + window.btoa(this.props.username + ":" + this.props.password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },).catch((error) => {
                this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
                alert(error.toString().substr(4) + "!")
            })
        if( !this.state.isCatchError && response.ok ) {
            //console.log(response)
            const grades = [...this.state.grades]
            const index = grades.findIndex((grade) => grade.id === this.state.selectedGrade.id)
            grades.splice(index, 1, this.state.selectedGrade)
            this.setState({ grades, selectedGrade: null, isLoading: false })
            alert("Grade changed.")
        } else if (!this.state.isCatchError) {
            this.setState({ selectedGrade: null, isLoading: false, isError: true })
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
        }
    }

    handleNewSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        let grades = null
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/grades/', {
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(this.state.newGrade),
            headers: {
                'Authorization': 'Basic ' + window.btoa(username + ":" + password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },).catch((error) => {
                alert(error.toString().substr(4) + "!")
                this.setState({ isLoading: false, newGrade: null, isCatchError: true, errMsg: error.toString().substr(4) })
            })
        if( !this.state.isCatchError && response.ok ) {
            /*const grades = [...this.state.grades]
            grades.push(this.state.newGrade)
            this.setState({ grades, newGrade: null, isLoading: false })*/
            
            const response = await fetch('http://localhost:8080/project/grades/teacher/' /*+ '?useLegacyDatetimeCode=false'*/, {
            mode: 'cors',
            method: 'GET',
            headers: {
                'Authorization': 'Basic ' + window.btoa(username + ":" + password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },).catch((error) => {
                this.setState({ isLoading: false, newGrade: null, isCatchError: true, errMsg: error.toString().substr(4) })
                alert(error.toString().substr(4) + "!")
            })
            if( !this.state.isCatchError && response.ok ) {
                grades = await response.json()
                //this.setState({ gradesAlone: grades, newGrade: null })


                //console.log(this.state.gradesAlone)
            } else if (!this.state.isCatchError) {
                //alert(response.statusText)
                //alert(response.console.error())
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
                this.setState({ /*gradesAlone: null,*/ newGrade: null, isLoading: false, isError: true })
            }

            if( !this.state.isCatchError && !this.state.isError) {
                const response1 = await fetch('http://localhost:8080/project/subjects/teacher/students/' /*+ '?useLegacyDatetimeCode=false'*/, {
                mode: 'cors',
                method: 'GET',
                headers: {
                    'Authorization': 'Basic ' + window.btoa(username + ":" + password),
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8', 
                    'Origin': 'http://localhost:3000', }
                },).catch((error) => {
                    this.setState({ isLoading: false, newGrade: null, isCatchError: true, errMsg: error.toString().substr(4) })
                    alert(error.toString().substr(4) + "!")
                })
                if( !this.state.isCatchError && response1.ok ) {
                    const subjects = await response1.json();
                    const updateSubjects = []
                    //console.log(subjects)
                    for (let i = 0; i<subjects.length; i++) {
                        updateSubjects.push({student: subjects[i].student,  semester: "FIRST_MIDTERM", teacher_subject_department: subjects[i].teacher_subject_department });
                    }
                    for (let i = 0; i<subjects.length; i++) {
                        updateSubjects.push({student: subjects[i].student,  semester: "SECOND_MIDTERM", teacher_subject_department: subjects[i].teacher_subject_department });
                    }
                    alert("Grade added.")
                    //console.log(updateSubjects)
                    this.setState({ grades: grades.concat(updateSubjects), newGrade: null, isLoading: false })
                    //console.log(this.state.grades)
                } else if (!this.state.isCatchError) {
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
                    this.setState({ /*gradesAlone: null,*/ newGrade: null, isLoading: false, isError: true })
                }
            }
        } else if (!this.state.isCatchError) {
            this.setState({ newGrade: null, isLoading: false, isError: true })
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
            //alert(response.statusText)
        }
    }

    handleChange = (e) => {
        e.preventDefault()
        this.state.selectedGrade ? this.setState({
            selectedGrade: {
              ...this.state.selectedGrade,
              [e.target.name]: (e.target.validity.valid) ? ( e.target.type === 'checkbox' ? e.target.checked : e.target.value ) : alert("Grade must be from 1 to 5!")
            }
        }) : 
            this.setState({
                newGrade: {
                ...this.state.newGrade,
                [e.target.name]: (e.target.validity.valid) ? ( e.target.type === 'checkbox' ? e.target.checked : e.target.value ) : alert("Grade must be from 1 to 5!")
                }
        })
    }

    handleDelete = async (gradeId) => {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/grades/'+Number(gradeId), {
            mode: 'cors',
            method: 'DELETE',
            headers: {
                'Authorization': 'Basic ' + window.btoa(this.props.username + ":" + this.props.password),
                'Accept': 'application/json; charset=UTF-8',
                'Content-Type': 'application/json; charset=UTF-8', 
                'Origin': 'http://localhost:3000', }
            },)
            .catch((error) => {
                //console.log(error)
                this.setState({ isLoading: false, isCatchError: true, errMsg: error.toString().substr(4) })
                alert(error.toString().substr(4) + "!")
            })
        if( !this.state.isCatchError && response.ok ) {
                //const changedUser = await response.json();
            const grades = this.state.grades.filter((grade) => grade.id !== gradeId)
            this.setState({ grades, isLoading: false })
            alert("Grade deleted.")
        } else if (!this.state.isCatchError) {
            //console.log(response.text())
            /*response.text().then(function(data) {
                //console.log(data);
              });*/
            //console.log(response.headers.get('Authorization'))
            //this.props.history.push('/teachers/teachergrades')
            this.setState({ newGrade: null, selectedGrade: null, isLoading: false, isError: true })
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
        }
    }

    handleCancel = () => { this.setState({ selectedGrade: null, newGrade: null }); }


    render() {

        const {grades, selectedGrade, newGrade } = this.state

        /*if( isError ) {
            return <div>Error!</div>
        }*/


        return grades.length > 0 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    { newGrade && 
                        <div>
                            <StudentGradesForm 
                                {...newGrade}
                                /*gradeValue={newGrade.gradeValue}
                                subjectName={newGrade.subjectName}*/
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleNewSubmit}
                            />
                        </div> 
                    }

                    { selectedGrade && 
                        <div>
                            <StudentGradesForm 
                                {...selectedGrade}
                                /*gradeValue={selectedGrade.gradeValue}
                                subjectName={selectedGrade.teacher_subject_department.teachingSubject.subjectName}*/
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleEditSubmit}
                            />
                        </div> 
                    }


                    { !newGrade && !selectedGrade && 
                    <div>
                        {this.renderGradesByClDepStudSemSubj()}
                    </div> }
                    
                </React.Suspense>

            </div>
        )
        : null

    }
}

export default TeacherGrades