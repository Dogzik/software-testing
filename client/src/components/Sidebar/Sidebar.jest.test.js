import TestRenderer from 'react-test-renderer';
import Sidebar from './Sidebar.js';

describe('Sidebar', () => {
  test('clicking write', () => {
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
