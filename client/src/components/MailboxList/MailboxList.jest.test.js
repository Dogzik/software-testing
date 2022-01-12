import TestRenderer from 'react-test-renderer';
import MailboxList from './MailboxList.js';

describe('MailboxList', () => {
  test('selecting mailbox', () => {
    const mailboxes = [{ name: 'one' }, { name: 'two' }, { name: 'three' }];
    const onSelectMailbox = jest.fn();
    const root = TestRenderer.create(<MailboxList mailboxes={mailboxes} onSelectMailbox={onSelectMailbox}/>).root;
    const mailboxList = root.findAllByType('li');
    mailboxList.forEach((mailbox, idx) => {
      expect(mailbox.children[0]).toEqual(mailboxes[idx].name);
      mailbox.props.onClick(null);
    });
    expect(onSelectMailbox.mock.calls).toHaveLength(mailboxes.length);
    onSelectMailbox.mock.calls.forEach((call, idx) => {
      expect(call).toEqual([idx]);
    });
  });
});
