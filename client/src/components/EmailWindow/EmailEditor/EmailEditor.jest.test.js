import TestRenderer from 'react-test-renderer';
import EmailEditor from './EmailEditor.js';
import { CLIENT_EPIC } from '../../utils/Utils.js';

describe('EmailEditor', () => {
  const address = 'aaaa@bbb.ccc';
  const subject = 'ssssuuuubbbjjjeeeccttt';
  const text = 'ttttteeeeeeexxxxxxxttttt';

  function allureInfo(story) {
    reporter.epic(CLIENT_EPIC);
    reporter.feature('EmailEditor');
    reporter.story(story);
  }

  test('display correct data', () => {
    allureInfo('Just render data');
    const root = TestRenderer.create(<EmailEditor from={'from'} onSend={jest.fn()} postClient={{ sendEmail: jest.fn() }}/>).root;
    const [addressInput, subjectInput] = root.findAllByType('input');
    const textInput = root.findByType('textarea');
    addressInput.props.onChange({ target: { value: address } });
    subjectInput.props.onChange({ target: { value: subject } });
    textInput.props.onChange({ target: { value: text } });
    expect(addressInput.props.value).toEqual(address);
    expect(subjectInput.props.value).toEqual(subject);
    expect(textInput.props.value).toEqual(text);
  });

  test('correctly sending email', async () => {
    allureInfo('Sending email');
    const mockClient = {
      sendEmail: jest.fn(async (from, to, subject, text) => 'new_id'),
    };
    const onSend = jest.fn(async () => {});
    const from = 'from@from.from';

    const root = TestRenderer.create(<EmailEditor from={from} onSend={onSend} postClient={mockClient}/>).root;
    const [addressInput, subjectInput] = root.findAllByType('input');
    const textInput = root.findByType('textarea');
    const sendButton = root.findByType('button');
    addressInput.props.onChange({ target: { value: address } });
    subjectInput.props.onChange({ target: { value: subject } });
    textInput.props.onChange({ target: { value: text } });
    await sendButton.props.onClick(null);
    expect(mockClient.sendEmail.mock.calls).toEqual([[from, address, subject, text]]);
    expect(onSend.mock.calls).toHaveLength(1);
  });

  test('handling null controller response', async () => {
    allureInfo('Null controller response');
    const mockClient = {
      sendEmail: jest.fn(async (from, to, subject, text) => null),
    };
    const onSend = jest.fn(async () => {});

    const root = TestRenderer.create(<EmailEditor from={''} onSend={onSend} postClient={mockClient}/>).root;
    const sendButton = root.findByType('button');
    await sendButton.props.onClick(null);
    expect(onSend.mock.calls).toHaveLength(0);
  });
});
