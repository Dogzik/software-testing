import TestRenderer from 'react-test-renderer';
import MailboxList from './MailboxList.js';
import { CLIENT_EPIC } from '../utils/Utils.js';

describe('MailboxList', () => {
  function allureInfo(story) {
    reporter.epic(CLIENT_EPIC);
    reporter.feature('MailboxList');
    reporter.story(story);
  }

  test('selecting mailbox', () => {
    allureInfo('Selecting mailbox');
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
