import './EmailList.css'
import React from 'react';
import EmailListItem from '../EmailListItem/EmailListItem';

class EmailList extends React.Component {
  render() {
    const {emails, selectedEmailId, onSelectEmail} = this.props;
    if (emails.length === 0) {
      return (
        <div className="EmailList empty">
          Nothing to see here
        </div>
      );
    }
    return (
      <div className={"EmailList"}>{
        emails.map(
          email => <EmailListItem
                    key={email.id}
                    email={email}
                    selected={email.id === selectedEmailId}
                    onSelectEmail={onSelectEmail}/>
        )
      }</div>
    )
  }
}

export default EmailList;
