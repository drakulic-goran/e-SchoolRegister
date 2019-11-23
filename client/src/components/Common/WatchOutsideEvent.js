import React, { Component } from 'react'

export class WatchOutsideEvent extends Component {

    componentDidMount() {
        document.body.addEventListener('click', this.handleClick);
        document.body.addEventListener('mouseover', this.handleLeave);
    }
    
    componentUnmount() {
        document.body.removeEventListener('click', this.handleClick);
        document.body.removeEventListener('mouseover', this.handleLeave);
    }

    componentDidUpdate() {
        document.body.addEventListener('mouseover', this.handleLeave);
    }

    handleClick = (e) => {
        let {container} = this.refs; 
        const {onClickOutside} = this.props; 
        const {target} = e; 
        if (typeof onClickOutside !== 'function') {
            return;
        }
        if (target !== container && !container.contains(target)) {
            onClickOutside(e); 
        }
        onClickOutside(e); 
    }

    handleLeave = (e) => {
        let {container} = this.refs; 
        const {onMouseLeaveOutside} = this.props; 
        const {target} = e; 
        if (typeof onMouseLeaveOutside !== 'function') {
            return;
        }
        if (target !== container && !container.contains(target)) {
            onMouseLeaveOutside(e); 
        }
    }


    render() {
        return (
            <span ref="container">
                {this.props.children}
            </span>
        );
    }
}

export default WatchOutsideEvent
