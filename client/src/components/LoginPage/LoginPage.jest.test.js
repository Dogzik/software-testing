import TestRenderer from 'react-test-renderer';
import LoginPage from './LoginPage.js';

describe('LoginPage', () => {
  const FAULTY_CONTROLLER = {
    async checkAuthentication(type, login, password) {
      return {
        ok: false,
        errorMsg: 'Error',
      };
    },
  };

  test.each([['Sign in'], ['Sign up']])('failed %s via button', async (name) => {
    const onSign = jest.fn();
    const root = TestRenderer.create(<LoginPage postClient={FAULTY_CONTROLLER} onSign={onSign}/>).root;
    const signButton = root.find((el) => (el.type === 'button') && (el.children[0] === name));
    const buttonEvent = { preventDefault: jest.fn() };
    window.alert = jest.fn();
    await signButton.props.onClick(buttonEvent);
    expect(onSign.mock.calls).toHaveLength(0);
    expect(buttonEvent.preventDefault.mock.calls).toHaveLength(1);
    expect(window.alert.mock.calls).toEqual([['Error']]);
  });

  test('failed Sign in via form', async () => {
    const onSign = jest.fn();
    const root = TestRenderer.create(<LoginPage postClient={FAULTY_CONTROLLER} onSign={onSign}/>).root;
    const signInForm = root.findByType('form');
    window.alert = jest.fn();
    await signInForm.props.onSubmit(null);
    expect(onSign.mock.calls).toHaveLength(0);
    expect(window.alert.mock.calls).toEqual([['Error']]);
  });

  test.each([['Sign in'], ['Sign up']])('correct authentication via %s button', async (name) => {
    const mockAuthenticator = jest.fn(async (_, login, password) => {return { ok: true };});
    const mockController = { checkAuthentication: mockAuthenticator };
    const onSign = jest.fn();
    const login = 'kek';
    const password = 'lol';

    const root = TestRenderer.create(<LoginPage postClient={mockController} onSign={onSign}/>).root;
    const [loginInput, passwordInput] = root.findAllByType('input');
    loginInput.props.onChange({ target: { value: login } });
    passwordInput.props.onChange({ target: { value: password } });
    const signButton = root.find((el) => (el.type === 'button') && (el.children[0] === name));
    const buttonEvent = { preventDefault: jest.fn() };
    await signButton.props.onClick(buttonEvent);
    expect(onSign.mock.calls).toEqual([[login]]);
    expect(buttonEvent.preventDefault.mock.calls).toHaveLength(1);
    expect(mockAuthenticator.mock.calls).toHaveLength(1);
    expect(mockAuthenticator.mock.calls[0].slice(1)).toEqual([login, password]);
  });

  test('correct authentication via sign in form', async () => {
    const mockAuthenticator = jest.fn(async (_, login, password) => {return { ok: true };});
    const mockController = { checkAuthentication: mockAuthenticator };
    const onSign = jest.fn();
    const login = 'kek';
    const password = 'lol';

    const root = TestRenderer.create(<LoginPage postClient={mockController} onSign={onSign}/>).root;
    const [loginInput, passwordInput] = root.findAllByType('input');
    loginInput.props.onChange({ target: { value: login } });
    passwordInput.props.onChange({ target: { value: password } });

    const signInForm = root.findByType('form');
    await signInForm.props.onSubmit(null);
    expect(onSign.mock.calls).toEqual([[login]]);
    expect(mockAuthenticator.mock.calls).toHaveLength(1);
    expect(mockAuthenticator.mock.calls[0].slice(1)).toEqual([login, password]);
  });

  test('correct credentials display', () => {
    const login = 'kek';
    const password = 'lol';

    const root = TestRenderer.create(<LoginPage postClient={FAULTY_CONTROLLER} onSign={jest.fn()}/>).root;
    const [loginInput, passwordInput] = root.findAllByType('input');
    loginInput.props.onChange({ target: { value: login } });
    passwordInput.props.onChange({ target: { value: password } });
    expect(loginInput.props.value).toEqual(login);
    expect(passwordInput.props.value).toEqual(password);
  });
});
