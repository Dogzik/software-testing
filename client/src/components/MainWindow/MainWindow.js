import React from 'react';
import LoginPage from '../LoginPage/LoginPage';
import App from '../App/App';

class MainWindow extends React.Component {
  constructor(props) {
    super(props);
    this.state = { user: null };
    this.onSign = this.onSign.bind(this);
  }

  onSign(user) {
    this.setState({ user: user });
  }

  render() {
    const user = this.state.user;
    const postClient = this.props.postClient;
    if (user == null) {
      return <LoginPage postClient={postClient} onSign={this.onSign}/>;
    }
    return <App user={user} postClient={postClient}/>;
  }
}

export default MainWindow;
