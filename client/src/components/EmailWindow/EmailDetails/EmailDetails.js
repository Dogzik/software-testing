import './EmailDetails.css';
import '../EmailWindow.css'
import React from 'react';
import { getEmailDate, getEmailTime } from '../../utils/Utils.js';

class EmailDetails extends React.Component {
  render() {
    const email = this.props.email;
    if (email == null) {
      return <div className={'EmailDetails'}/>;
    }
    const date = `${getEmailDate(email.time)} · ${getEmailTime(email.time)}`;

    return (
      <div className={'EmailWindow'}>
        <div className={'EmailDetails__header'}>
          <h3 className={'EmailDetails__subject'}>{email.subject}</h3>
          <div>
            <span className={'EmailDetails__address'}>{email.address}</span>
            <span className={'EmailDetails__time'}>{date}</span>
          </div>
        </div>
        <div className={'EmailDetails__text'}>{email.text}</div>
      </div>
    );
  }
}

export default EmailDetails;
