import './LoginPage.css';
import React from 'react';

class LoginPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      login: '',
      password: '',
    };
    this.onLoginChange = this.onLoginChange.bind(this);
    this.onPasswordChange = this.onPasswordChange.bind(this);
    this.onSign = this.onSign.bind(this);
    this.onSignIn = this.onSignIn.bind(this);
    this.onSignUp = this.onSignUp.bind(this);
    this.onSignInClick = this.onSignInClick.bind(this);
    this.onSignUpClick = this.onSignUpClick.bind(this);
  }

  onLoginChange(e) {
    this.setState({ login: e.target.value });
  }

  onPasswordChange(e) {
    this.setState({ password: e.target.value });
  }

  async onSign(type) {
    const { postClient, onSign } = this.props;
    const { login, password } = this.state;
    const loginResult = await postClient.checkAuthentication(type, login, password);
    if (loginResult.ok) {
      onSign(login);
    } else {
      alert(loginResult.errorMsg);
    }
  }

  async onSignIn() {
    await this.onSign('login');
  }

  async onSignUp() {
    await this.onSign('register');
  }

  async onSignInClick(e) {
    e.preventDefault();
    await this.onSignIn();
  }

  async onSignUpClick(e) {
    e.preventDefault();
    await this.onSignUp();
  }

  render() {
    const { login, password } = this.state;
    return (
      <form className={'LoginPage'} onSubmit={this.onSignIn}>
        <h1 className={'LoginPage__welcome'}>Welcome to Govnopochta</h1>
        <div className={'LoginPage__input'}>
          <label htmlFor={"login"}>Login:</label>
          <input id={"login"} value={login} placeholder={'login'} onChange={this.onLoginChange}/>
        </div>
        <div className={'LoginPage__input'}>
          <label htmlFor={"password"}>Password:</label>
          <input id={"password"} value={password} placeholder={'password'} onChange={this.onPasswordChange}/>
        </div>
        <div className={'LoginPage__actions'}>
          <button onClick={this.onSignInClick}>Sign in</button>
          <button onClick={this.onSignUpClick}>Sign up</button>
        </div>
      </form>
    );
  }
}

export default LoginPage;
