import TestRenderer from 'react-test-renderer';
import EmailListItem from './EmailListItem.js';
import { CLIENT_EPIC, getEmailDate } from '../utils/Utils.js';

describe('EmailListItem', () => {
  const email = {
    id: 'id1',
    subject: 'subject',
    time: Date.parse('2022-10-20') / 1000,
    address: 'aaaaa@bbbb.cccc',
  };

  function allureInfo(story) {
    reporter.epic(CLIENT_EPIC);
    reporter.feature('EmailListItem');
    reporter.story(story);
  }

  test('render simple email', () => {
    allureInfo('Render unselected email');
    const root = TestRenderer.create(<EmailListItem email={email} selected={false} onSelectEmail={jest.fn()}/>).root;
    const [subject, details] = root.children[0].children;
    expect(subject.children[0]).toEqual(email.subject);
    expect(details.children[0].children[0]).toEqual(email.address);
    expect(details.children[1].children[0]).toEqual(getEmailDate(email.time));
  });

  test('clicking on email', () => {
    allureInfo('Click on unselected email');
    const onSelect = jest.fn();
    const root = TestRenderer.create(<EmailListItem email={email} selected={false} onSelectEmail={onSelect}/>).root;
    const clickableDiv = root.children[0];
    clickableDiv.props.onClick(null);
    const calls = onSelect.mock.calls;
    expect(calls.length).toEqual(1);
    expect(calls[0]).toEqual([email]);
  });

  test('rendering selected email', () => {
    allureInfo('Render selected email');
    const root = TestRenderer.create(<EmailListItem email={email} selected={true} onSelectEmail={jest.fn()}/>).root;
    expect(root.children[0].props.className).toEqual('EmailListItem selected');
  });
});
