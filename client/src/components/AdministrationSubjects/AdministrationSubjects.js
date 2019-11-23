import React, { Component } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEdit, faTrash, faUserPlus, faCompactDisc, faChalkboard, faPlusCircle, faEyeSlash, faEye } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/Administration.css'
import AssignUnassignClasses from '../AssignUnassign/AssignUnassignClasses';
import SubjectForm from '../SubjectForm/SubjectForm';


class AdministrationSubjects extends Component {
    constructor(props) {
        super(props)
        this.state = {
            loggedUser: this.props.loggedUser,
            subjects: [],
            selectedSubject: null,
            newSubject: null,
            savingSubject: null,
            isLoading: false,
            isError: false,
            errMsg: '',
            isCatchError: false,
            classes: [],
            selectedSubjectForClasses: null,
            activeSubjectView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/subjects/', {
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
            const subjects = await response.json();
            //console.log(subjects)


            const response1 = await fetch('http://localhost:8080/project/class/', {
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
            if( !this.state.isCatchError && response1.ok ) {
                const classes = await response1.json();
                this.setState({ classes: classes })
                //console.log(this.state.classes)
            } else if (!this.state.isCatchError) {
                this.setState({ isLoading: false, isError: true })
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
            }

            this.setState({ subjects: subjects, isLoading: false })
            //console.log(this.state.subjects)
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

    shouldComponentUpdate(nextProps, nextState) {
        if (this.state !== nextState) {
            return true;
        }
        return false;
    }

    handleSubjectsClick = (id) => {
        this.state.activeSubjectView !== id ?
            this.setState({activeSubjectView: id})
        : this.setState({activeSubjectView: null})
    }

    renderSubjectData() {
        return this.state.subjects.map((subject, id) => (
            <div className="administration-header" key={subject.id}><h2>{subject.subjectName}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeSubjectView !== id ? faEye : faEyeSlash} onClick={() => this.handleSubjectsClick(id)} /></h2>
                <div className={this.state.activeSubjectView === id ? "" : "hide"}>
                    <div className="administration-assign">Assigned classes: 
                        {subject.classes.map((classe, id1) =>
                        <span key={id1}>
                            <div className="user-info" key={classe.clas.id}> {classe.clas.classLabel}
                            </div>
                        </span> )}
                        <button type="button"
                                    name="onChangeClasses"
                                    onClick={ () => this.handleEditSubjects(subject.id) }
                                    id="change_btn">
                            <FontAwesomeIcon icon={faChalkboard} /> 
                            <span> Change</span>
                        </button>
                    </div>
                    <span className="user-info"><h4>Week number of classes</h4><FontAwesomeIcon icon={faCompactDisc} spin /> {subject.weekClassesNumber}
                    </span>
                    <div>
                        <button type="button"
                                    name="onDelete"
                                    onClick={ () => this.handleDelete(subject.id) }
                                    id="edit_delete_btn">
                            <FontAwesomeIcon icon={faTrash} /> 
                            <span> DELETE</span>
                        </button>
                        <button type="button"
                                    name="onEdit"
                                    onClick={ () => this.handleEdit(subject.id) }
                                    id="edit_delete_btn">
                            <FontAwesomeIcon icon={faEdit} />
                            <span> EDIT</span>
                        </button>
                    </div>
                </div>
            </div>
        ))
    }

    handleDelete = async (subjectId) => {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/subjects/'+subjectId, {
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
            const subjects = this.state.subjects.filter((subject) => subject.id !== subjectId)
            this.setState({ subjects, isLoading: false, activeSubjectView: null })
            alert("Subject deleted.")
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

    handleEdit = (subjectId) => { const subject = this.state.subjects.find( su => su.id === subjectId ); this.setState({ selectedSubject: subject }); }

    handleEditSubmit = async (e) => {
        e.preventDefault()
        //console.log(this.state.selectedSubject)
        //console.log(this.state.savingSubject)
        this.setState({ isLoading: true })
        //console.log(this.state.selectedSubject)
        const response = await fetch('http://localhost:8080/project/subjects/'+this.state.selectedSubject.id, {
            mode: 'cors',
            method: 'PUT',
            body: JSON.stringify(this.state.savingSubject),
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
            const subjects = [...this.state.subjects]
            const index = subjects.findIndex((user) => user.id === this.state.selectedSubject.id)
            subjects.splice(index, 1, this.state.selectedSubject)
            this.setState({ subjects, selectedSubject: null, savingSubject: null, isLoading: false })
            alert("Subject changed.")
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

    handleNew = () => { this.setState({newSubject: { } }); }

    handleNewSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        //console.log(this.state.newSubject)
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/subjects/', {
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(this.state.newSubject),
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
            const response = await fetch('http://localhost:8080/project/subjects/', {
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
                const subjects = await response.json();
                //console.log(subjects)

                const response1 = await fetch('http://localhost:8080/project/class/', {
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
                if( !this.state.isCatchError && response1.ok ) {
                    const classes = await response1.json();
                    this.setState({ classes: classes })
                    //console.log(this.state.classes)
                } else if (!this.state.isCatchError) {
                    this.setState({ isLoading: false, isError: true })
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
                }

                this.setState({ subjects: subjects, newSubject: null, isLoading: false })
                //console.log(this.state.subjects)
                alert("Subject added.")
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
            this.setState({ newSubject: null, isLoading: false, isError: true })
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
        this.state.selectedSubject ? 
            this.setState({
                selectedSubject: {
                ...this.state.selectedSubject,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                },
                savingSubject: {
                ...this.state.savingSubject,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                }
        }) : 
            this.setState({
                newSubject: {
                ...this.state.newSubject,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                }
        })
    }

    handleCancel = () => { this.setState({ selectedSubject: null, newSubject: null, savingSubject: null, selectedSubjectForClasses: null }); }

    handleEditSubjects = (subjectID) => { 
        const subject = this.state.subjects.find( su => su.id === subjectID ); 
        const subjectClasses = [...subject.classes]
        let classes = []
        subjectClasses.forEach(clas => classes = [...classes, clas.clas])
        this.setState({ selectedSubjectForClasses: {...subject, classes: classes, students: classes} }); 
    }

    handleSubmitClasses = async (classes) => {
        this.setState({ isLoading: true })
        //console.log(this.state.selectedSubjectForClasses)
        let classesRemove = this.state.selectedSubjectForClasses.classes.filter(x => !classes.includes(x));
        let classesAdd = classes.filter(x => !this.state.selectedSubjectForClasses.classes.includes(x));
        //console.log(classesRemove)
        //console.log(classesAdd)
        if (classesRemove.length > 0 || classesAdd.length > 0) {
            if (classesRemove.length > 0) {
                const remove = {learningProgram: "2020", classes: []}
                classesRemove.forEach(clas => remove.classes = [...remove.classes, `${clas.id}`])
                //console.log(remove)
                const response = await fetch('http://localhost:8080/project/subjects/'+this.state.selectedSubjectForClasses.id+'/remove-class/', {
                    mode: 'cors',
                    method: 'PUT',
                    body: JSON.stringify(remove),
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
                    //const removedClasses = await response.json();
                    let remainclasses = [...this.state.selectedSubjectForClasses.classes]
                    classesRemove.forEach(removedClass => remainclasses = remainclasses.filter((cl) => cl.id !== removedClass.id))               
                    let remainFormatedClasses = []
                    remainclasses.forEach(cl => remainFormatedClasses = [...remainFormatedClasses, {clas: cl}])
                    this.setState({ selectedSubjectForClasses: {...this.state.selectedSubjectForClasses, classes: remainFormatedClasses} })
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
                //console.log("REMOVE")
                //console.log(this.state.selectedSubjectForClasses)
            }
            if (classesAdd.length > 0) {
                const add = {learningProgram: "2020", classes: []}
                classesAdd.forEach(clas => add.classes = [...add.classes, `${clas.id}`])
                //console.log(add)
                const response = await fetch('http://localhost:8080/project/subjects/'+this.state.selectedSubjectForClasses.id+'/add-class/', {
                    mode: 'cors',
                    method: 'PUT',
                    body: JSON.stringify(add),
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
                    let remainclasses = [...this.state.selectedSubjectForClasses.classes, ...classesAdd]
                    let remainFormatedClasses = []
                    remainclasses.forEach(cl => remainFormatedClasses = [...remainFormatedClasses, {clas: cl}])
                    this.setState({ selectedSubjectForClasses: {...this.state.selectedSubjectForClasses, classes: remainFormatedClasses} })
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
            //console.log("ADD")
            //console.log(this.state.selectedSubjectForClasses)  
            const subjects = [...this.state.subjects]
            const index = subjects.findIndex((subject) => subject.id === this.state.selectedSubjectForClasses.id)
            subjects.splice(index, 1, this.state.selectedSubjectForClasses)
            this.setState({ subjects })
            alert("Classes for subject changed.")
        }
        else {
            alert("No changes submit.")
        }
        this.setState({ selectedSubjectForClasses: null, isLoading: false }); 
    }


    render() {

        const {subjects, selectedSubject, newSubject, classes, selectedSubjectForClasses} = this.state

        return subjects.length > 0 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    { !newSubject && !selectedSubject && !selectedSubjectForClasses &&
                        <div>
                            <button type="button"
                                        name="onAddNew"
                                        onClick={this.handleNew}
                                        id="add_btn">
                                <FontAwesomeIcon icon={faPlusCircle} />
                                <span> Add new</span>
                            </button>

                            <h1 id='title'>LIST OF SUBJECTS</h1>

                            <div>
                                {this.renderSubjectData()}
                            </div>
                        </div>
                    }

                    { selectedSubjectForClasses &&
                        <div>
                            <h1>Classes assignments</h1>
                            <AssignUnassignClasses students={[...classes]} parent={{...selectedSubjectForClasses }} name={selectedSubjectForClasses.subjectName} editChildren={this.handleSubmitClasses} onCancel={this.handleCancel} />
                        </div>
                    }

                    { newSubject && 
                        <div>
                            <SubjectForm 
                                {...newSubject}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleNewSubmit} /> 
                        </div>
                    }

                    { selectedSubject && 
                        <div>
                            <SubjectForm 
                                {...selectedSubject}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleEditSubmit} /> 
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

export default AdministrationSubjects