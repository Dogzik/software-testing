import React from 'react';
import './Sidebar.css';
import MailboxList from '../MailboxList/MailboxList';

class Sidebar extends React.Component {
  render() {
    return (
      <div className={'Sidebar'}>
        <div className={'Sidebar__write'} onClick={() => {this.props.onClickWrite();}}>{'Write'}</div>
        <MailboxList mailboxes={this.props.mailboxes} onSelectMailbox={this.props.onSelectMailbox}/>
        <div className={'Sidebar__user'}>{this.props.user}</div>
      </div>
    );
  }
}

export default Sidebar;
