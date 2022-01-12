import TestRenderer from 'react-test-renderer';
import EmailDetails from './EmailDetails.js';
import { getEmailDate, getEmailTime } from '../../utils/Utils.js';

describe('EmailDetails', () => {
  test('no email', () => {
    const root = TestRenderer.create(<EmailDetails email={null}/>).root;
    expect(root.children[0].children).toHaveLength(0);
    expect(root.findByType('div').props.className).toEqual('EmailDetails');
  });

  test('render email', () => {
    const date = Date.parse('2012-11-15 14:22');
    const email = {
      time: date / 1000,
      address: 'kek@lol.com',
      subject: 'email subject',
      text: 'Some email text',
    };
    const root = TestRenderer.create(<EmailDetails email={email}/>).root;
    const subject = root.findByType('h3');
    expect(subject.children[0]).toEqual(email.subject);
    const [address, time] = root.findAllByType('span');
    expect(address.children[0]).toEqual(email.address);
    expect(time.children[0]).toEqual(`${getEmailDate(email.time)} · ${getEmailTime(email.time)}`);
    expect(root.children[0].children[1].children[0]).toEqual(email.text);
  });
});
