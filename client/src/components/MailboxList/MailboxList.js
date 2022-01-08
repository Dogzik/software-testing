import React from 'react';
import './MailboxList.css';

class MailboxList extends React.Component {
  render() {
    const mailboxes = this.props.mailboxes.map((mailbox, idx) =>
      <li
        key={mailbox.name}
        className={'MailboxList__mailbox'}
        onClick={() => this.props.onSelectMailbox(idx)}
      >
        {mailbox.name}
      </li>,
    );
    return <ul className={'MailboxList'}>{mailboxes}</ul>;
  }
}

export default MailboxList;
