import React, { Component } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner, faEdit, faTrash, faUserPlus, faPlusCircle, faEyeSlash, faEye } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/Administration.css'
//import AssignUnassignClasses from '../AssignUnassign/AssignUnassignClasses';
import ClassForm from '../ClassForm/ClassForm';


class AdministrationClasses extends Component {
    constructor(props) {
        super(props)
        this.state = {
            loggedUser: this.props.loggedUser,
            selectedClass: null,
            newClass: null,
            savingClass: null,
            isLoading: false,
            isError: false,
            errMsg: '',
            isCatchError: false,
            classes: [],
            activeClassView: null
        }
    }

    async componentDidMount() {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/class/', {
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
            const classes = await response.json();
            this.setState({ classes: classes })
            //console.log(this.state.classes)
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

    handleClassesClick = (id) => {
        this.state.activeClassView !== id ?
            this.setState({activeClassView: id})
        : this.setState({activeClassView: null})
    }

    renderSubjectData() {
        return this.state.classes.map((clas, id) => (
            <div className="administration-header" key={clas.id}><h2>{clas.classLabel}  <FontAwesomeIcon className="cursor-pointer" icon={this.state.activeClassView !== id ? faEye : faEyeSlash} onClick={() => this.handleClassesClick(id)} /></h2>
                <div className={this.state.activeClassView === id ? "" : "hide"}>       
                    <div className="administration-assign">Assigned departments: 
                        {clas.departments.map((department, id1) =>
                        <span key={id1}> {department.department.status === 1 ? 
                        <div className="user-info" key={department.department.id}> {clas.classLabel}-{department.department.departmentLabel}
                            </div>
                        : null}
                        </span> )}
                    </div>
                    <div>
                        <button type="button"
                                    name="onDelete"
                                    onClick={ () => this.handleDelete(clas.id) }
                                    id="edit_delete_btn">
                            <FontAwesomeIcon icon={faTrash} /> 
                            <span> DELETE</span>
                        </button>
                        <button type="button"
                                    name="onDelete"
                                    onClick={ () => this.handleEdit(clas.id) }
                                    id="edit_delete_btn">
                            <FontAwesomeIcon icon={faEdit} />
                            <span> EDIT</span>
                        </button>
                    </div>
                </div>
            </div>
        ))
    }

    handleDelete = async (classId) => {
        this.setState({ isLoading: true })
        const response = await fetch('http://localhost:8080/project/class/'+classId, {
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
            const classes = this.state.classes.filter((subject) => subject.id !== classId)
            this.setState({ classes, isLoading: false, activeClassView: null })
            alert("Class deleted.")
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

    handleEdit = (classId) => { const clas = this.state.classes.find( cl => cl.id === classId ); this.setState({ selectedClass: clas }); }

    handleEditSubmit = async (e) => {
        e.preventDefault()
        //console.log(this.state.selectedClass)
        //console.log(this.state.savingClass)
        this.setState({ isLoading: true })
        //console.log(this.state.selectedClass)
        const response = await fetch('http://localhost:8080/project/class/'+this.state.selectedClass.id, {
            mode: 'cors',
            method: 'PUT',
            body: JSON.stringify(this.state.savingClass),
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
            const classes = [...this.state.classes]
            const index = classes.findIndex((user) => user.id === this.state.selectedClass.id)
            classes.splice(index, 1, this.state.selectedClass)
            this.setState({ classes, selectedClass: null, savingClass: null, isLoading: false })
            alert("Class changed.")
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

    handleNew = () => { this.setState({newClass: { } }); }

    handleNewSubmit = async (e) => {
        e.preventDefault()
        this.setState({ isLoading: true })
        //console.log(this.state.newClass)
        const { username, password } = this.props
        const response = await fetch('http://localhost:8080/project/class/', {
            mode: 'cors',
            method: 'POST',
            body: JSON.stringify(this.state.newClass),
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
            const response = await fetch('http://localhost:8080/project/class/', {
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
                const classes = await response.json();
                this.setState({ classes: classes, newClass: null, isLoading: false })
                //console.log(this.state.classes)
                alert("Class added.")
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
            this.setState({ newClass: null, isLoading: false, isError: true })
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
        this.state.selectedClass ? 
            this.setState({
                selectedClass: {
                ...this.state.selectedClass,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                },
                savingClass: {
                ...this.state.savingClass,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                }
        }) : 
            this.setState({
                newClass: {
                ...this.state.newClass,
                [e.target.name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
                }
        })
    }

    handleCancel = () => { this.setState({ selectedClass: null, newClass: null, savingClass: null }); }


    render() {

        const { selectedClass, newClass, classes } = this.state

        return classes.length > 0 
        ? (
            <div>

                <React.Suspense fallback={<h1><FontAwesomeIcon icon={faSpinner} spin /></h1>} id='susp'>

                    { !newClass && !selectedClass &&
                        <div>
                            <button type="button"
                                        name="onAddNew"
                                        onClick={this.handleNew}
                                        id="add_btn">
                                <FontAwesomeIcon icon={faPlusCircle} />
                                <span> Add new</span>
                            </button>

                            <h1 id='title'>LIST OF CLASSES</h1>

                            <div>
                                {this.renderSubjectData()}
                            </div>
                        </div>
                    }

                    { newClass && 
                        <div>
                            <ClassForm 
                                {...newClass}
                                onChange={this.handleChange} 
                                onCancel={this.handleCancel}
                                onSubmit = {this.handleNewSubmit} /> 
                        </div>
                    }

                    { selectedClass && 
                        <div>
                            <ClassForm 
                                {...selectedClass}
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

export default AdministrationClasses