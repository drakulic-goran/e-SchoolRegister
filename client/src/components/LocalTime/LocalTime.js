import React, { Component } from 'react'
//import Moment from 'react-moment';
import 'moment-timezone';

const moment = require('moment-timezone');

class LocalTime extends Component {
    formatLocalTime() {
        const t = this.props['value'];
        const f = this.props['format'];
        if (t && f) {
            //return moment.utc(t.substring(0, 10)).format(f).toUpperCase();
            //return moment(t).tz("Europe/Belgrade").format(f).toUpperCase();
            return moment(t).format(f).toUpperCase();
        } 
        return "";
    }

    render() {
        const formatLocalTime = this.formatLocalTime();
        return (
            <time>{formatLocalTime}</time>
        );
    }
}

export default LocalTime;