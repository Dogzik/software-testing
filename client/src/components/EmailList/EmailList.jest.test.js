import TestRenderer from 'react-test-renderer';
import EmailList from './EmailList.js';
import EmailListItem from '../EmailListItem/EmailListItem.js';
import { CLIENT_EPIC } from '../utils/Utils.js';

describe('EmailList', () => {
  function allureInfo(story) {
    reporter.epic(CLIENT_EPIC);
    reporter.feature('EmailList');
    reporter.story(story);
  }

  test('empty list', () => {
    allureInfo('Render empty list');
    const root = TestRenderer.create(<EmailList emails={[]} selectedEmailId={null} onSelectEmail={jest.fn()}/>).root;
    expect(root.children[0].props.className).toEqual('EmailList empty');
    expect(root.children[0].children[0]).toEqual('Nothing to see here');
  });

  test('non-empty list', () => {
    allureInfo('Render non-empty list');
    const emails = Array.from(Array(7).keys()).map(i => {return { id: i + 3 };});
    const root = TestRenderer.create(<EmailList emails={emails} selectedEmailId={null} onSelectEmail={jest.fn()}/>).root;
    const emailListItems = root.findAllByType(EmailListItem);
    expect(emailListItems.length).toEqual(emails.length);
    emailListItems.forEach((elem, idx) => {
      expect(elem.props.email).toEqual(emails[idx]);
    });
  });

  test('selection', () => {
    allureInfo('Selecting email from list');
    const emails = Array.from(Array(7).keys()).map(i => {return { id: i };});
    const selectedIdx = 4;
    const root = TestRenderer.create(<EmailList emails={emails} selectedEmailId={selectedIdx} onSelectEmail={jest.fn()}/>).root;
    const emailListItems = root.findAllByType(EmailListItem);
    expect(emailListItems[selectedIdx].props.selected).toBe(true);
  });
});
