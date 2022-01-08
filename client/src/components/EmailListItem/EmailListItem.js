import './EmailListItem.css';
import React from 'react';
import { getEmailDate } from '../utils/Utils.js';

class EmailListItem extends React.Component {
  render() {
    const { email, selected, onSelectEmail } = this.props;
    const className = 'EmailListItem' + (selected ? (' selected') : '');
    return (
      <div className={className} onClick={() => {onSelectEmail(email);}}>
        <div className={'EmailListItem__subject truncate'}>{email.subject}</div>
        <div className={'EmailListItem__details'}>
          <span className={'EmailListItem__address truncate'}>{email.address}</span>
          <span className={'EmailListItem__time truncate'}>{getEmailDate(email.time)}</span>
        </div>
      </div>
    );
  }
}

export default EmailListItem;
