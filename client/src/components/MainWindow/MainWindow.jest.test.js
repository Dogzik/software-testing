import TestRenderer from 'react-test-renderer';
import MainWindow from './MainWindow.js';
import LoginPage from '../LoginPage/LoginPage.js';
import App from '../App/App.js';
import { CLIENT_EPIC } from '../utils/Utils.js';

describe('Main window', () => {
  const mockClient = {
    checkAuthentication: async () => {return { ok: true, errorMsg: null };},
    sendEmail: async () => 'id',
    fetchInbox: async () => [],
    fetchSent: async () => [],
  };

  function allureInfo(story) {
    reporter.epic(CLIENT_EPIC);
    reporter.feature('MainWindow');
    reporter.story(story);
  }

  test('initial login page', () => {
    allureInfo('Initial login page');
    const root = TestRenderer.create(<MainWindow postClient={mockClient}/>).root;
    const pages = root.findAllByType(LoginPage);
    expect(pages).toHaveLength(1);
  });

  test('successful login', async () => {
    allureInfo('Successful login');
    const root = TestRenderer.create(<MainWindow postClient={mockClient}/>).root;
    const loginPage = root.findByType(LoginPage);
    const user = 'pepe@frog.com';
    loginPage.props.onSign(user);
    const app = root.findByType(App);
    expect(app.props.user).toEqual(user);
  });
});
