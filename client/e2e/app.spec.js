import { test, expect } from '@playwright/test';
import { generateString, submitCredentials } from './utils.js';

const postTest = test.extend({
  user: async ({}, runTest) => {
    const value = generateString(10);
    await runTest(value);
  },
  pass: async ({}, runTest) => {
    const value = generateString(10);
    await runTest(value);
  },
  loggedPage: async ({ user, pass, page }, runTest) => {
    await page.goto('http://localhost:3000');
    await submitCredentials(page, 'Sign up', user, pass);
    await page.waitForSelector('.Sidebar__write');
    await runTest(page);
  },
});

async function sendEmail(loggedPage, to, subject, text) {
  await loggedPage.waitForSelector('.Sidebar__write');
  const writeButton = await loggedPage.$('text=Write');
  await writeButton.click();

  // await loggedPage.waitForSelector('.EmailEditor__info');
  const [addressInput, subjectInput] = await loggedPage.$$('input');
  const textInput = await loggedPage.$('textarea');
  await addressInput.fill(to);
  await subjectInput.fill(subject);
  await textInput.fill(text);

  const sendButton = await loggedPage.$('.EmailEditor__send');
  await sendButton.click();
}

function genEmail(from, to) {
  return {
    from: from,
    to: to,
    subject: generateString(10),
    text: generateString(20),
  };
}

async function getListItemDetails(listItem) {
  const realSubject = await (await listItem.$('.EmailListItem__subject')).innerText();
  const realAddress = await (await listItem.$('.EmailListItem__address')).innerText();
  return {
    address: realAddress,
    subject: realSubject,
  };
}

async function getSelectedEmailDetails(page) {
  const realSubject = await (await page.$('.EmailDetails__subject')).innerText();
  const realAddress = await (await page.$('.EmailDetails__address')).innerText();
  const realText = await (await page.$('.EmailDetails__text')).innerText();
  return {
    address: realAddress,
    subject: realSubject,
    text: realText,
  };
}

async function clickButton(page, button) {
  await button.click();
  await page.waitForTimeout(100);
}

postTest('Empty new user', async ({ loggedPage }) => {
  const inboxList = await loggedPage.$('.EmailList');
  const inboxText = await inboxList.innerText();
  expect(inboxText).toEqual('Nothing to see here');
  const sentButton = await loggedPage.$('text=Sent');
  await clickButton(loggedPage, sentButton);
  const sentList = await loggedPage.$('.EmailList');
  const sentText = await sentList.innerText();
  expect(sentText).toEqual('Nothing to see here');
});

postTest('Sending message for new user', async ({ loggedPage }) => {
  const address = generateString(8);
  const subject = generateString(14);
  const text = generateString(40);
  await sendEmail(loggedPage, address, subject, text);

  await loggedPage.waitForSelector('.EmailEditor__info');
  const sentButton = await loggedPage.$('text=Sent');
  await sentButton.click();

  await loggedPage.waitForTimeout(400);
  const sentList = await loggedPage.$$('.EmailListItem');
  expect(sentList).toHaveLength(1);
  const realItem = await getListItemDetails(sentList[0]);
  expect(realItem.address).toEqual(address);
  expect(realItem.subject).toEqual(subject);

  await clickButton(loggedPage, sentList[0]);
  const selectedEmail = await getSelectedEmailDetails(loggedPage);
  expect(selectedEmail.address).toEqual(address);
  expect(selectedEmail.subject).toEqual(subject);
  expect(selectedEmail.text).toEqual(text);
});

postTest('Login to non-empty mailbox', async ({ page }) => {
  const user1 = generateString(7);
  const pass1 = generateString(7);
  const user2 = generateString(7);
  const pass2 = generateString(7);

  const email1 = genEmail(user1, user2);
  const email2 = genEmail(user1, user2);
  const email3 = genEmail(user2, user1);
  const email4 = genEmail(user2, user1);

  await page.goto('http://localhost:3000');

  await submitCredentials(page, 'Sign up', user1, pass1);
  await sendEmail(page, user2, email1.subject, email1.text);
  await page.waitForTimeout(1000);
  await sendEmail(page, user2, email2.subject, email2.text);
  await page.waitForTimeout(1000);

  await page.reload();

  await submitCredentials(page, 'Sign up', user2, pass2);
  await sendEmail(page, user1, email3.subject, email3.text);
  await page.waitForTimeout(1000);
  await sendEmail(page, user1, email4.subject, email4.text);

  await page.reload();

  await submitCredentials(page, 'Sign in', user1, pass1);
  await page.waitForTimeout(400);
  const inboxList = await page.$$('.EmailListItem');
  expect(inboxList).toHaveLength(2);
  const inbox1 = await getListItemDetails(inboxList[0]);
  const inbox2 = await getListItemDetails(inboxList[1]);
  expect(inbox1).toEqual({ subject: email4.subject, address: user2 });
  expect(inbox2).toEqual({ subject: email3.subject, address: user2 });

  await clickButton(page, inboxList[0]);
  const selectedInbox1 = await getSelectedEmailDetails(page);
  expect(selectedInbox1).toEqual({
    address: user2,
    subject: email4.subject,
    text: email4.text,
  });
  await clickButton(page, inboxList[1]);
  const selectedInbox2 = await getSelectedEmailDetails(page);
  expect(selectedInbox2).toEqual({
    address: user2,
    subject: email3.subject,
    text: email3.text,
  });

  await page.click('text=Sent');
  await page.waitForTimeout(100);
  const sentList = await page.$$('.EmailListItem');
  expect(sentList).toHaveLength(2);
  const sent1 = await getListItemDetails(sentList[0]);
  const sent2 = await getListItemDetails(sentList[1]);
  expect(sent1).toEqual({ subject: email2.subject, address: user2 });
  expect(sent2).toEqual({ subject: email1.subject, address: user2 });

  await clickButton(page, sentList[0]);
  const selectedSent1 = await getSelectedEmailDetails(page);
  expect(selectedSent1).toEqual({
    address: user2,
    subject: email2.subject,
    text: email2.text,
  });
  await clickButton(page, sentList[1]);
  const selectedSent2 = await getSelectedEmailDetails(page);
  expect(selectedSent2).toEqual({
    address: user2,
    subject: email1.subject,
    text: email1.text,
  });
});
