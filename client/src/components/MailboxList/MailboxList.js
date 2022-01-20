import React from 'react';
import './MailboxList.css';
import { ENTER_KEY } from '../utils/Utils.js';

class MailboxList extends React.Component {
  render() {
    const mailboxes = this.props.mailboxes.map((mailbox, idx) =>
      <li
        tabIndex={0}
        key={mailbox.name}
        className={'MailboxList__mailbox'}
        onClick={() => this.props.onSelectMailbox(idx)}
        onKeyDown={(e) => {
          if (e.keyCode === ENTER_KEY) {
            this.props.onSelectMailbox(idx)
          }
        }}
      >
        {mailbox.name}
      </li>,
    );
    return <ul className={'MailboxList'}>{mailboxes}</ul>;
  }
}

export default MailboxList;
