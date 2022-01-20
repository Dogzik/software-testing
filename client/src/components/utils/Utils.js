export function getEmailDate(unixTimestamp) {
  const date = new Date(unixTimestamp * 1000);
  const year = date.getFullYear();
  const month = '0' + date.getMonth();
  const day = '0' + date.getDate();
  return `${year}/${month.substr(-2)}/${day.substr(-2)}`;
}

export function getEmailTime(unixTimestamp) {
  const date = new Date(unixTimestamp * 1000);
  const hours = '0' + date.getHours();
  const minutes = '0' + date.getMinutes();
  return `${hours.substr(-2)}:${minutes.substr(-2)}`;
}

export function toInternalEmail(rawEmail, inbox) {
  return {
    id: rawEmail.id,
    address: inbox ? rawEmail.from : rawEmail.to,
    subject: rawEmail.subject,
    time: rawEmail.time,
    text: rawEmail.text,
  };
}

export const CLIENT_EPIC = 'Client component tests';
export const ENTER_KEY = 13;
