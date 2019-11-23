import React from 'react'
import {Route, Redirect} from 'react-router-dom';


export default ({render, isLoggedIn, isAuthorized, ...rest}) => {
    return (
        <Route
            {...rest}
            render={ (props) => (isLoggedIn && isAuthorized)
            ? (
                render(props)
            ) : (
                <Redirect to={{
                    pathname: "/",
                    //pathname: path,
                    state: {from: props.location}
                }} />
            )
            }
        />
    )
}