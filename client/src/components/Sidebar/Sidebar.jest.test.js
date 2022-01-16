import TestRenderer from 'react-test-renderer';
import Sidebar from './Sidebar.js';
import { CLIENT_EPIC } from '../utils/Utils.js';

describe('Sidebar', () => {
  function allureInfo(story) {
    reporter.epic(CLIENT_EPIC);
    reporter.feature('Sidebar');
    reporter.story(story);
  }

  test('clicking write', () => {
    allureInfo('Clicking write');
    const user = 'lolo@keke.net';
    const onClickWrite = jest.fn();
    const root = TestRenderer.create(<Sidebar user={user} onClickWrite={onClickWrite} mailboxes={[]} onSelectMailbox={jest.fn()}/>).root;
    const writeDiv = root.children[0].children[0];
    expect(writeDiv.children[0]).toEqual('Write');
    writeDiv.props.onClick(null);
    expect(onClickWrite.mock.calls).toHaveLength(1);
    expect(root.children[0].children[2].children[0]).toEqual(user);
  });
});
