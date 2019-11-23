import React, { Component } from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleDown, faAngleDoubleUp, faAngleDoubleDown, faAngleUp, faSave, faTimes } from '@fortawesome/free-solid-svg-icons';
import '../../Styles/AssignUnassign.css'


export class AssignUnassignClasses extends Component {

    constructor(props) {
        super(props)
        this.state = {
            parent: { ...this.props.parent },
            selectedToAssign: [],
            selectedToUnassign: []
        }
    }

    isSelected = (id) => {
        const ids = [...this.state.selectedToAssign, ...this.state.selectedToUnassign]
        return ids.some(item => item.id === id.id)
    }

    handleAssign = () => {
        const parent = { ...this.state.parent }
        this.state.selectedToAssign.forEach(id => parent.students = [...parent.students, id])//parent.students.push(id))
        this.setState({ parent: parent, selectedToAssign: [], selectedToUnassign: [] })
    }

    handleAssignAll = () => {
        const parent = { ...this.state.parent }
        const ids = [...this.state.parent.students]
        const students = this.props.students.filter(student => !ids.some(item => item.id === student.id ))
        parent.students = [...parent.students, ...students]
        this.setState({ parent: parent, selectedToAssign: [], selectedToUnassign: [] })
    }

    handleRemove = () => {
        const parent = { ...this.state.parent }
        this.state.selectedToUnassign.forEach(id => parent.students = parent.students.filter(cid => cid.id !== id.id))
        this.setState({ parent: parent, selectedToAssign: [], selectedToUnassign: [] })
    }

    handleRemoveAll = () => {
        const parent = { ...this.state.parent }
        parent.students = []
        this.setState({ parent: parent, selectedToAssign: [], selectedToUnassign: [] })
    }

    handleUnassignedClick = (id) => {
        if (this.state.selectedToUnassign.some(item => item.id === id.id)) {
            const newSelectedToUnassign = [...this.state.selectedToUnassign.filter(el => el !== id)]
            this.setState({ selectedToUnassign: newSelectedToUnassign })
        } else {
            const newSelectedToUnassign = [...this.state.selectedToUnassign, id]
            this.setState({ selectedToUnassign: newSelectedToUnassign })
        }
    }

    handleAssignedClick = (id) => {
        if (this.state.selectedToAssign.some(item => item.id === id.id)) {
            const newSelectedToAssign = [...this.state.selectedToAssign.filter(el => el !== id)]
            this.setState({ selectedToAssign: newSelectedToAssign })
        } else {
            const newSelectedToAssign = [...this.state.selectedToAssign, id]
            this.setState({ selectedToAssign: newSelectedToAssign })
        }
    }

    assignedStudents = () => {
        //console.log("+++++++++++++++++++")
        //console.log(this.state.parent)
        const ids = [...this.state.parent.students]
        //console.log(ids)
        //console.log(this.props.students)
        const students = this.props.students.filter(student => ids.some(item => item.id === student.id))
        //console.log(students)
        return students.map(stud => (
        <li 
            key={stud.id} 
            onClick={() => this.handleUnassignedClick(stud)}
            className={this.isSelected(stud) ? 'selected' : undefined}
        >{stud.classLabel}</li>
        ))
    }

    unassignedStudents = () => {
        //console.log("-------------------")
        //console.log(this.state.parent)
        const ids = [...this.state.parent.students]
        //console.log(ids)
        //console.log(this.props.students)
        const students = this.props.students.filter(student => !ids.some(item => item.id === student.id ))
        //console.log(students)
        return students.map(stud => (
        <li
            key={stud.id}
            onClick={() => this.handleAssignedClick(stud)}
            className={this.isSelected(stud) ? 'selected' : undefined}
        >{stud.classLabel}</li>
        ))
    }

    onAssignSubmit = () => {
        this.props.editChildren(this.state.parent.students)
    }

    render() {
        return (
            <div>
                <div className='student'>
                    <div className='students'>
                    <h3 className='students_title'>Available</h3>
                    <ul>
                        {this.unassignedStudents()}
                    </ul>
                    </div>
                    <div className='student_assignment'>
                        <span onClick={this.handleAssignAll}>
                            <FontAwesomeIcon icon={faAngleDoubleDown} />
                        </span> 
                        <span onClick={this.handleAssign}>
                            <FontAwesomeIcon icon={faAngleDown} />
                        </span> 
                        <span onClick={this.handleRemove}>
                            <FontAwesomeIcon icon={faAngleUp} />
                        </span>
                        <span onClick={this.handleRemoveAll}>
                            <FontAwesomeIcon icon={faAngleDoubleUp} />
                        </span>
                    </div>
                    <div className='parent_students'>
                    <h3 className='students_title'>{this.props.name}</h3>
                    <ul>
                        {this.assignedStudents()}
                    </ul>
                    </div>
                </div>
                <div>
                    <button type="button"
                                name="onCancel"
                                onClick={this.props.onCancel}
                                id="reg_btn">
                        <FontAwesomeIcon icon={faTimes} />
                        <span> Cancel</span>
                    </button>
                    <button type="button"
                                name="onParentSubmit"
                                onClick={this.onAssignSubmit}
                                id="reg_btn">
                        <FontAwesomeIcon icon={faSave} />
                        <span> Save</span>
                    </button>
                </div>
            </div>
        )
    }
}

export default AssignUnassignClasses