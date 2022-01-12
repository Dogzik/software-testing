import { test, expect } from '@playwright/test';
import { generateString } from './utils.js';

async function submitCredentials(page, type, login, password) {
  const [loginInput, passwordInput] = await page.$$('.LoginPage__input input');
  await loginInput.fill(login);
  await passwordInput.fill(password);
  const singInButton = await page.$(`text=${type}`);
  await singInButton.click();
}

test.describe('Registration', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:3000');
  });

  test('unknown user can not sign in', async ({ page }) => {
    await page.on('dialog', async (dialog) => {
      const msg = await dialog.message();
      expect(msg).toEqual('Incorrect credentials');
    });
    await submitCredentials(page, 'Sign in', generateString(10), generateString(10));
    await page.waitForEvent('dialog');
  });

  test('new user can sign up', async ({ page }) => {
    await submitCredentials(page, 'Sign up', generateString(10), generateString(10));
    const writeButton = await page.waitForSelector('.Sidebar__write');
    const buttonText = await writeButton.innerText();
    expect(buttonText).toEqual('Write');
  });

  test('registered user can sign in', async ({ page }) => {
    const login = generateString(10);
    const password = generateString(10);
    await submitCredentials(page, 'Sign up', login, password);
    await page.waitForSelector('.Sidebar__write');
    await page.reload();
    await submitCredentials(page, 'Sign in', login, password);
    const writeButton = await page.waitForSelector('.Sidebar__write');
    const buttonText = await writeButton.innerText();
    expect(buttonText).toEqual('Write');
  });

  test('registered user uses wrong password', async ({ page }) => {
    await page.on('dialog', async (dialog) => {
      const msg = await dialog.message();
      expect(msg).toEqual('Incorrect credentials');
    });
    const login = generateString(10);
    const password = generateString(10);
    await submitCredentials(page, 'Sign up', login, password);
    await page.waitForSelector('.Sidebar__write');
    await page.reload();
    await submitCredentials(page, 'Sign in', login, password + '_suffix');
    await page.waitForEvent('dialog');
  });

  test('user can not sign up twice', async ({ page }) => {
    const login = generateString(10);
    const password = generateString(10);
    await page.on('dialog', async (dialog) => {
      const msg = await dialog.message();
      expect(msg).toEqual(`User ${login} already exists`);
    });
    await submitCredentials(page, 'Sign up', login, password);
    await page.waitForSelector('.Sidebar__write');
    await page.reload();
    await submitCredentials(page, 'Sign up', login, password + '_suffix');
    await page.waitForEvent('dialog');
  });
});
