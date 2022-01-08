import './App.css';
import React from 'react';
import Sidebar from '../Sidebar/Sidebar';
import EmailList from '../EmailList/EmailList';
import EmailDetails from '../EmailWindow/EmailDetails/EmailDetails';
import EmailEditor from '../EmailWindow/EmailEditor/EmailEditor';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedMailboxIdx: 0,
      selectedEmail: null,
      hasInput: false,
      mailboxes: [
        { name: 'Inbox', emails: [] },
        { name: 'Sent', emails: [] },
      ],
    };
    this.onSelectMailbox = this.onSelectMailbox.bind(this);
    this.onSelectEmail = this.onSelectEmail.bind(this);
    this.onClickWrite = this.onClickWrite.bind(this);
    this.loadMailboxes = this.loadMailboxes.bind(this);
    this.onSend = this.onSend.bind(this);
  }

  onSelectMailbox(idx) {
    const { selectedMailboxIdx, selectedEmail } = this.state;
    const newSelectedEmail = (idx === selectedMailboxIdx) ? selectedEmail : null;
    this.setState({
      selectedMailboxIdx: idx,
      selectedEmail: newSelectedEmail,
    });
  }

  onSelectEmail(email) {
    this.setState({
      selectedEmail: email,
      hasInput: false,
    });
  }

  onClickWrite() {
    this.setState({
      selectedEmail: null,
      hasInput: true,
    });
  }

  async loadMailboxes() {
    const { user, postClient } = this.props;
    return await Promise.all([
      postClient.fetchInbox(user),
      postClient.fetchSent(user),
    ]);
  }

  async onSend() {
    const [inboxEmails, sentEmails] = await this.loadMailboxes();
    this.setState({
      selectedEmail: null,
      hasInput: false,
      mailboxes: [
        { name: 'Inbox', emails: inboxEmails },
        { name: 'Sent', emails: sentEmails },
      ],
    });
  }

  async componentDidMount() {
    const [inboxEmails, sentEmails] = await this.loadMailboxes();
    this.setState({
      mailboxes: [
        { name: 'Inbox', emails: inboxEmails },
        { name: 'Sent', emails: sentEmails },
      ],
    });
  }

  render() {
    const { user, postClient } = this.props;
    const { selectedMailboxIdx, selectedEmail, hasInput, mailboxes } = this.state;
    const selectedEmailId = (selectedEmail === null) ? null : selectedEmail.id;
    return (
      <div className={'App'}>
        <Sidebar
          user={user}
          mailboxes={mailboxes}
          onSelectMailbox={this.onSelectMailbox}
          onClickWrite={this.onClickWrite}
        />
        <EmailList
          emails={mailboxes[selectedMailboxIdx].emails}
          selectedEmailId={selectedEmailId}
          onSelectEmail={this.onSelectEmail}
        />
        {hasInput
          ? <EmailEditor from={user} onSend={this.onSend} postClient={postClient}/>
          : <EmailDetails email={selectedEmail}/>
        }
      </div>
    );
  }
}

export default App;
