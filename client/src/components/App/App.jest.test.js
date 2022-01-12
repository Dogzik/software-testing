import TestRenderer from 'react-test-renderer';
import App from './App.js';
import EmailDetails from '../EmailWindow/EmailDetails/EmailDetails.js';
import Sidebar from '../Sidebar/Sidebar.js';
import EmailList from '../EmailList/EmailList.js';
import EmailEditor from '../EmailWindow/EmailEditor/EmailEditor.js';
import waitForExpect from 'wait-for-expect';

describe('App', () => {
  const USER = 'from@user.user';
  const SAMPLE_INBOX = [
    {
      id: 'id1',
      from: 'from1',
      to: USER,
      subject: 'subject1',
      text: 'text1',
      time: Date.parse('2022-11-12 22:22') / 1000,
    },
    {
      id: 'id2',
      from: 'from2',
      to: USER,
      subject: 'subject2',
      text: 'text2',
      time: Date.parse('2022-11-9 22:22') / 1000,
    },
  ];
  const SAMPLE_SENT = [
    {
      id: 'id3',
      from: USER,
      to: 'to3',
      subject: 'subject3',
      text: 'text3',
      time: Date.parse('2021-11-12 20:22') / 1000,
    },
    {
      id: 'id4',
      from: USER,
      to: 'to4',
      subject: 'subject4',
      text: 'text4',
      time: Date.parse('2000-8-20 10:15') / 1000,
    },
  ];

  const mockClient = {
    checkAuthentication: async () => {return { ok: true, errorMsg: null };},
    sendEmail: async () => 'new_id',
    fetchInbox: async () => SAMPLE_INBOX,
    fetchSent: async () => SAMPLE_SENT,
  };

  async function waitForFullMount(root) {
    await waitForExpect(() => {
      expect(root.findByType(EmailList).props.emails).not.toHaveLength(0);
    });
  }

  test('initial render', async () => {
    const root = TestRenderer.create(<App user={USER} postClient={mockClient}/>).root;
    await waitForFullMount(root);
    const emailDetails = root.findByType(EmailDetails);
    expect(emailDetails.props.email).toBeNull();
    const sidebar = root.findByType(Sidebar);
    expect(sidebar.props.user).toEqual(USER);
    expect(sidebar.props.mailboxes).toEqual([
      { name: 'Inbox', emails: SAMPLE_INBOX },
      { name: 'Sent', emails: SAMPLE_SENT }]);
    const emailsList = root.findByType(EmailList);
    expect(emailsList.props.emails).toEqual(SAMPLE_INBOX);
    expect(emailsList.props.selectedEmailId).toBeNull();
  });

  test('opening editor', async () => {
    const root = TestRenderer.create(<App user={USER} postClient={mockClient}/>).root;
    await waitForFullMount(root);
    const sidebar = root.findByType(Sidebar);
    sidebar.props.onClickWrite();
    const editor = root.findByType(EmailEditor);
    expect(editor.props.from).toEqual(USER);
  });

  test('selecting sent mailbox', async () => {
    const root = TestRenderer.create(<App user={USER} postClient={mockClient}/>).root;
    await waitForFullMount(root);
    const sidebar = root.findByType(Sidebar);
    sidebar.props.onSelectMailbox(1);
    const emailsList = root.findByType(EmailList);
    expect(emailsList.props.emails).toEqual(SAMPLE_SENT);
    expect(emailsList.props.selectedEmailId).toBeNull();
  });

  test('selecting email', async () => {
    const root = TestRenderer.create(<App user={USER} postClient={mockClient}/>).root;
    await waitForFullMount(root);
    const emailsList = root.findByType(EmailList);
    emailsList.props.onSelectEmail(emailsList.props.emails[1]);
    const emailDetails = root.findByType(EmailDetails);
    expect(emailDetails.props.email).toEqual(emailsList.props.emails[1]);
    expect(emailsList.props.selectedEmailId).toEqual(emailsList.props.emails[1].id);
  });

  test('updating mailboxes after write', async () => {
    const newSent = [
      ...SAMPLE_SENT,
      {
        id: 'id5',
        from: USER,
        to: 'to5',
        subject: 'subject5',
        text: 'text5',
        time: Date.parse('1998-8-20 10:15') / 1000,
      },
    ];
    const updatingMockClient = {
      checkAuthentication: async () => {return { ok: true, errorMsg: null };},
      sendEmail: async () => 'new_id',
      fetchInbox: jest.fn().mockResolvedValue(SAMPLE_INBOX),
      fetchSent: jest.fn().mockResolvedValueOnce(SAMPLE_SENT).mockResolvedValueOnce(newSent),
    };

    const root = TestRenderer.create(<App user={USER} postClient={updatingMockClient}/>).root;
    await waitForFullMount(root);
    const sidebar = root.findByType(Sidebar);
    sidebar.props.onClickWrite();
    const editor = root.findByType(EmailEditor);
    await editor.props.onSend();

    expect(updatingMockClient.fetchInbox.mock.calls).toHaveLength(2);
    expect(updatingMockClient.fetchSent.mock.calls).toHaveLength(2);

    const emailDetails = root.findByType(EmailDetails);
    expect(emailDetails.props.email).toBeNull();

    root.instance.onSelectMailbox(1);
    const emailList = root.findByType(EmailList);
    expect(emailList.props.emails).toEqual(newSent);
  });
});
