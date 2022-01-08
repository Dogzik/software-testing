import { toInternalEmail } from './Utils.js';

const FAILED_AUTHENTICATION = {
  ok: false,
  errorMsg: 'Failed to connect to server',
};

const SUCCESSFUL_AUTHENTICATION = {
  ok: true,
  errorMsg: null,
};

export default class PostClient {
  constructor(host, port, protocol = 'http') {
    this.url = `${protocol}://${host}:${port}`;
  }

  async checkAuthentication(type, login, password) {
    try {
      const rawResponse = await fetch(
        `${this.url}/${type}`,
        {
          cache: 'no-cache',
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ login: login, password: password }),
        },
      );
      if (rawResponse.ok) {
        return SUCCESSFUL_AUTHENTICATION;
      }
      return {
        ok: false,
        errorMsg: await rawResponse.text(),
      };
    } catch (e) {
      console.log(e);
      return FAILED_AUTHENTICATION;
    }
  }

  async sendEmail(from, to, subject, text) {
    const email = {
      from: from,
      to: to,
      subject: subject,
      text: text,
    };
    try {
      const response = await fetch(
        `${this.url}/send_email`,
        {
          cache: 'no-cache',
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(email),
        },
      );
      return response.ok ? await response.text() : null;
    } catch (e) {
      console.log(e);
      return null;
    }
  }

  async fetchMailboxEmails(type, user) {
    try {
      const response = await fetch(
        `${this.url}/get/${type}?user=${user}`,
        {
          cache: 'no-cache',
          method: 'GET',
          headers: {
            'Accept': 'application/json',
          },
        },
      );
      if (!response.ok) {
        console.log(response);
        return [];
      }
      const rawMailbox = await response.json();
      return rawMailbox.map((email) => toInternalEmail(email, type === 'inbox'));
    } catch (e) {
      console.log(e);
      return [];
    }
  }

  async fetchInbox(user) {
    return this.fetchMailboxEmails('inbox', user);
  }

  async fetchSent(user) {
    return this.fetchMailboxEmails('sent', user);
  }
}
