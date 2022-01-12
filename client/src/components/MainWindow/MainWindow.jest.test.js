import TestRenderer from 'react-test-renderer';
import MainWindow from './MainWindow.js';
import LoginPage from '../LoginPage/LoginPage.js';
import App from '../App/App.js';

describe('Main window', () => {
  const mockClient = {
    checkAuthentication: async () => {return { ok: true, errorMsg: null };},
    sendEmail: async () => 'id',
    fetchInbox: async () => [],
    fetchSent: async () => [],
  };

  test('initial login page', () => {
    const root = TestRenderer.create(<MainWindow postClient={mockClient}/>).root;
    const pages = root.findAllByType(LoginPage);
    expect(pages).toHaveLength(1);
  });

  test('successful login', async () => {
    const root = TestRenderer.create(<MainWindow postClient={mockClient}/>).root;
    const loginPage = root.findByType(LoginPage);
    const user = 'pepe@frog.com';
    loginPage.props.onSign(user);
    const app = root.findByType(App);
    expect(app.props.user).toEqual(user);
  });
});
