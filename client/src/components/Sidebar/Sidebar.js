import React from 'react';
import './Sidebar.css';
import MailboxList from '../MailboxList/MailboxList';
import { ENTER_KEY } from '../utils/Utils.js';

class Sidebar extends React.Component {
  constructor(props) {
    super(props);
    this.onEnter = this.onEnter.bind(this);
    this.onClick = this.onClick.bind(this);
  }

  onEnter(e) {
    if (e.keyCode === ENTER_KEY) {
      this.props.onClickWrite();
    }
  }

  onClick() {
    this.props.onClickWrite();
  }

  render() {
    return (
      <div className={'Sidebar'}>
        <div tabIndex={0} className={'Sidebar__write'} onClick={this.onClick} onKeyDown={this.onEnter}>{'Write'}</div>
        <MailboxList mailboxes={this.props.mailboxes} onSelectMailbox={this.props.onSelectMailbox}/>
        <div className={'Sidebar__user'}>{this.props.user}</div>
      </div>
    );
  }
}

export default Sidebar;
