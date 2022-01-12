import { getEmailDate, getEmailTime, toInternalEmail } from './Utils.js';

describe('getEmailDate', () => {
  test('return correct date from timestamp', () => {
    const year = 1337;
    const month = 11;
    const date = 28;
    const fullDate = new Date();
    fullDate.setFullYear(year, month, date);
    const actual = getEmailDate(fullDate.getTime() / 1000);
    expect(actual).toEqual(`${year}/${month}/${date}`);
  });

  test('return same date for different time', () => {
    const year = 2000;
    const month = 10;
    const date = 20;
    const firstDate = new Date();
    firstDate.setFullYear(year, month, date);
    firstDate.setHours(11, 11, 11);
    const secondDate = new Date(firstDate.getTime());
    secondDate.setHours(10, 10, 10);
    const firstActual = getEmailDate(firstDate.getTime() / 1000);
    const secondActual = getEmailDate(secondDate.getTime() / 1000);
    expect(firstActual).toEqual(secondActual);
    expect(firstActual).toEqual(`${year}/${month}/${date}`);
  });

  test('return month with leafing zeros', () => {
    const year = 2200;
    const month = 7;
    const date = 15;
    const fullDate = new Date();
    fullDate.setFullYear(year, month, date);
    const actual = getEmailDate(fullDate.getTime() / 1000);
    const monthStr = ('0' + month).substr(-2);
    expect(actual).toEqual(`${year}/${monthStr}/${date}`);
  });

  test('return date with leafing zeros', () => {
    const year = 2010;
    const month = 11;
    const date = 4;
    const fullDate = new Date();
    fullDate.setFullYear(year, month, date);
    const actual = getEmailDate(fullDate.getTime() / 1000);
    const dateStr = ('0' + date).substr(-2);
    expect(actual).toEqual(`${year}/${month}/${dateStr}`);
  });
});

describe('getEmailTime', () => {
  test('return correct time from timestamp', () => {
    const hours = 14;
    const minutes = 15;
    const fullDate = new Date();
    fullDate.setHours(hours, minutes);
    const actual = getEmailTime(fullDate.getTime() / 1000);
    expect(actual).toEqual(`${hours}:${minutes}`);
  });

  test('return same time for different date', () => {
    const hours = 22;
    const minutes = 10;
    const firstDate = new Date();
    firstDate.setHours(hours, minutes);
    firstDate.setFullYear(1945, 10, 22);
    const secondDate = new Date(firstDate.getTime());
    secondDate.setFullYear(2022, 5, 13);
    const firstActual = getEmailTime(firstDate.getTime() / 1000);
    const secondActual = getEmailTime(secondDate.getTime() / 1000);
    expect(firstActual).toEqual(secondActual);
    expect(firstActual).toEqual(`${hours}:${minutes}`);
  });

  test('return hours with leading zeros', () => {
    const hours = 4;
    const minutes = 20;
    const fullDate = new Date();
    fullDate.setHours(hours, minutes);
    const actual = getEmailTime(fullDate.getTime() / 1000);
    const hoursStr = ('0' + hours).substr(-2);
    expect(actual).toEqual(`${hoursStr}:${minutes}`);
  });

  test('return minutes with leading zeros', () => {
    const hours = 22;
    const minutes = 8;
    const fullDate = new Date();
    fullDate.setHours(hours, minutes);
    const actual = getEmailTime(fullDate.getTime() / 1000);
    const minutesStr = ('0' + minutes).substr(-2);
    expect(actual).toEqual(`${hours}:${minutesStr}`);
  });
});

describe('toInternalEmail', () => {
  const rawEmail = {
    id: 'id',
    from: 'aaa',
    to: 'bbb',
    subject: 'subject',
    time: 13123123,
    text: 'text',
  };

  test('return inbox email', () => {
    const actual = toInternalEmail(rawEmail, true);
    const expected = {
      id: rawEmail.id,
      address: rawEmail.from,
      subject: rawEmail.subject,
      time: rawEmail.time,
      text: rawEmail.text,
    };
    expect(actual).toEqual(expected);
  });

  test('return sent email', () => {
    const actual = toInternalEmail(rawEmail, false);
    const expected = {
      id: rawEmail.id,
      address: rawEmail.to,
      subject: rawEmail.subject,
      time: rawEmail.time,
      text: rawEmail.text,
    };
    expect(actual).toEqual(expected);
  });
});
