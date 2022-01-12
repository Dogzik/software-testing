const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

export function delay(time) {
  return new Promise(res => setTimeout(res, time));
}

export function generateString(length) {
  let result = '';
  const charactersLength = characters.length;
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }
  return result;
}

export async function submitCredentials(page, type, login, password) {
  const [loginInput, passwordInput] = await page.$$('.LoginPage__input input');
  await loginInput.fill(login);
  await passwordInput.fill(password);
  const singInButton = await page.$(`text=${type}`);
  await singInButton.click();
}
