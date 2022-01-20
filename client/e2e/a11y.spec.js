import { test } from '@playwright/test';
import pkg from 'axe-playwright';
import { generateString, submitCredentials } from './utils.js';

const { checkA11y, injectAxe } = pkg;

async function testA11t(page, options = null) {
  await injectAxe(page);
  await checkA11y(page, null, null, options);
}

test.describe('A11y', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:3000');
  });

  test('Login page', async ({ page }) => {
    await testA11t(page);
  });

  test('App page', async ({ page }) => {
    const user = generateString(10);
    const pass = generateString(10);
    await submitCredentials(page, 'Sign up', user, pass);
    await page.waitForTimeout(300);
    const options = {
      rules: {
        'page-has-heading-one': { enabled: false },
      },
    };
    await testA11t(page, options);
  });
});
