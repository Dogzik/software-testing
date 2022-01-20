import './EmailListItem.css';
import React from 'react';
import { ENTER_KEY, getEmailDate } from '../utils/Utils.js';

class EmailListItem extends React.Component {
  onClick = () => {
    const { email, onSelectEmail } = this.props;
    onSelectEmail(email);
  }

  onEnter = (e) => {
    if (e.keyCode === ENTER_KEY) {
      this.onClick()
    }
  }

  render() {
    const { email, selected } = this.props;
    const className = 'EmailListItem' + (selected ? (' selected') : '');
    return (
      <div tabIndex={0} className={className} onClick={this.onClick} onKeyDown={this.onEnter}>
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
