import React from 'react';
import { Route } from 'react-router-dom';
import PrivateRoute from './PrivateRoute';


export function NestedPrivateRoute(props) {
    return (
        <PrivateRoute  exact={props.exact} path={props.path} render={ p => <props.component {...p} children={props.children} /> } />
    )
}

